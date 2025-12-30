package com.atguigu.yygh.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.enums.OrderStatusEnum;
import com.atguigu.yygh.enums.PaymentStatusEnum;
import com.atguigu.yygh.hosp.client.HospFeignClient;
import com.atguigu.yygh.model.order.OrderInfo;
import com.atguigu.yygh.model.order.PaymentInfo;
import com.atguigu.yygh.model.user.Patient;
import com.atguigu.yygh.mq.MqConst;
import com.atguigu.yygh.mq.RabbitService;
import com.atguigu.yygh.order.mapper.OrderInfoMapper;
import com.atguigu.yygh.order.service.OrderInfoService;
import com.atguigu.yygh.order.service.PaymentInfoService;
import com.atguigu.yygh.order.service.WeiPayService;
import com.atguigu.yygh.order.utils.HttpRequestHelper;
import com.atguigu.yygh.user.client.PatientFeignClient;
import com.atguigu.yygh.vo.hosp.ScheduleOrderVo;
import com.atguigu.yygh.vo.msm.MsmVo;
import com.atguigu.yygh.vo.order.OrderCountQueryVo;
import com.atguigu.yygh.vo.order.OrderCountVo;
import com.atguigu.yygh.vo.order.OrderMqVo;
import com.atguigu.yygh.vo.order.OrderQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author Henry Guan
 * @since 2023-05-05
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {
    @Autowired
    private HospFeignClient hospFeignClient;

    @Autowired
    private PatientFeignClient patientFeignClient;

    @Autowired
    private RabbitService rabbitService;

    @Autowired
    private WeiPayService weiPayService;

    @Autowired
    private PaymentInfoService paymentInfoService;

    @Override
    public Long submitOrder(String scheduleId, Long patientId) {
        //1.根据scheduleId获取医生排班信息
        ScheduleOrderVo schedule = hospFeignClient.getScheduleOrderVo(scheduleId);

        if (new DateTime(schedule.getStopTime()).isBeforeNow()) {
            throw new YyghException(20001,"已超过预约截至时间");
        }
        //2.根据patientId获取就诊人信息
        Patient patient = patientFeignClient.getPatient(patientId);
        //3.从平台请求第三方医院，确认当前用户能否挂号
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("hoscode",schedule.getHoscode());
        paramMap.put("depcode",schedule.getDepcode());
        paramMap.put("hosScheduleId",schedule.getHosScheduleId());
        paramMap.put("reserveDate",schedule.getReserveDate());
        paramMap.put("reserveTime",schedule.getReserveTime());
        paramMap.put("amount",schedule.getAmount());
        paramMap.put("sign", "");
        JSONObject jsonObject = HttpRequestHelper.sendRequest(paramMap, "http://localhost:9998/order/submitOrder");

        //3.1不能挂号，抛出异常；
        if (jsonObject == null || jsonObject.getInteger("code") != 200) {
            throw new YyghException(20001,"号源已满");
        }
        //3.2能挂号，将医生信息，就诊人信息、第三方医院返回信息存入order_info表
        JSONObject data = jsonObject.getJSONObject("data");
        //预约记录唯一标识（医院预约记录主键）
        String hosRecordId = data.getString("hosRecordId");
        //预约序号
        Integer number = data.getInteger("number");;
        //取号时间
        String fetchTime = data.getString("fetchTime");;
        //取号地址
        String fetchAddress = data.getString("fetchAddress");

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(patient.getUserId());
        //订单号
        String outTradeNo = System.currentTimeMillis() + ""+ new Random().nextInt(100);
        orderInfo.setOutTradeNo(outTradeNo);
        orderInfo.setHoscode(schedule.getHoscode());
        orderInfo.setHosname(schedule.getHosname());
        orderInfo.setDepcode(schedule.getDepcode());
        orderInfo.setDepname(schedule.getDepname());
        orderInfo.setTitle(schedule.getTitle());
        orderInfo.setScheduleId(schedule.getHosScheduleId());
        orderInfo.setReserveDate(schedule.getReserveDate());
        orderInfo.setReserveTime(schedule.getReserveTime());
        orderInfo.setPatientId(patientId);
        orderInfo.setPatientName(patient.getName());
        orderInfo.setPatientPhone(patient.getPhone());
        orderInfo.setHosRecordId(hosRecordId);
        orderInfo.setNumber(number);
        orderInfo.setFetchTime(fetchTime);
        orderInfo.setFetchAddress(fetchAddress);
        orderInfo.setAmount(schedule.getAmount());
        orderInfo.setQuitTime(schedule.getQuitTime());
        orderInfo.setOrderStatus(OrderStatusEnum.UNPAID.getStatus());

        baseMapper.insert(orderInfo);
        //3.3.更新平台对应的订单数
        OrderMqVo orderMqVo = new OrderMqVo();
        orderMqVo.setAvailableNumber(data.getIntValue("availableNumber"));
        orderMqVo.setReservedNumber(data.getIntValue("reservedNumber"));
        orderMqVo.setScheduleId(scheduleId);
        //短信提示
        MsmVo msmVo = new MsmVo();
        msmVo.setPhone(orderInfo.getPatientPhone());
        String reserveDate = new DateTime(orderInfo.getReserveDate()).toString("yyyy-MM-dd")
                        + (orderInfo.getReserveTime()==0 ? "上午": "下午");
        Map<String,Object> param = new HashMap<String,Object>(){{
            put("title", orderInfo.getHosname()+"|"+orderInfo.getDepname()+"|"+orderInfo.getTitle());
            put("amount", orderInfo.getAmount());
            put("reserveDate", reserveDate);
            put("name", orderInfo.getPatientName());
            put("quitTime", new DateTime(orderInfo.getQuitTime()).toString("yyyy-MM-dd HH:mm"));
        }};
        msmVo.setParam(param);
        //3.4.发送短信通知就诊人
        orderMqVo.setMsmVo(msmVo);
        rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_ORDER,MqConst.ROUTING_ORDER,orderMqVo);

        //4.返回订单id
        return orderInfo.getId();
    }

    @Override
    public Page<OrderInfo> getOrderInfoPage(Integer pageNum, Integer pageSize, OrderQueryVo orderQueryVo) {
        Page page = new Page(pageNum,pageSize);
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();

        if (!StringUtils.isEmpty(orderQueryVo.getUserId())) {
            queryWrapper.eq("user_id",orderQueryVo.getUserId());
        }
        if (!StringUtils.isEmpty(orderQueryVo.getOutTradeNo())) {
            queryWrapper.eq("out_trade_no",orderQueryVo.getOutTradeNo());
        }
        if (!StringUtils.isEmpty(orderQueryVo.getKeyword())) {
            queryWrapper.like("hosname",orderQueryVo.getKeyword());
        }
        if (!StringUtils.isEmpty(orderQueryVo.getPatientId())) {
            queryWrapper.eq("patient_id",orderQueryVo.getPatientId());
        }
        if (!StringUtils.isEmpty(orderQueryVo.getOrderStatus())) {
            queryWrapper.eq("order_status",orderQueryVo.getOrderStatus());
        }
        if (!StringUtils.isEmpty(orderQueryVo.getReserveDate())) {
            queryWrapper.ge("reserve_date",orderQueryVo.getReserveDate());
        }
        if (!StringUtils.isEmpty(orderQueryVo.getCreateTimeBegin())) {
            queryWrapper.ge("reserve_date",orderQueryVo.getCreateTimeBegin());
        }
        if (!StringUtils.isEmpty(orderQueryVo.getCreateTimeEnd())) {
            queryWrapper.le("reserve_date",orderQueryVo.getCreateTimeEnd());
        }
        Page<OrderInfo> page1 = baseMapper.selectPage(page, queryWrapper);
        page1.getRecords().parallelStream().forEach(item -> {
            this.packageOrderInfo(item);
        });
        return page1;
    }

    @Override
    public OrderInfo getOrders(Long orderId) {
        OrderInfo orderInfo = baseMapper.selectById(orderId);
        this.packageOrderInfo(orderInfo);
        return orderInfo;
    }

    @Override
    public void cancelOrder(Long orderId) {
        OrderInfo orderInfo = baseMapper.selectById(orderId);
        DateTime quitTime = new DateTime(orderInfo.getQuitTime());

        //1.确定当前取消预约的时间 和 挂号订单的取消预约截至时间 对比，判断是否超过
        if (quitTime.isBeforeNow()) {
            throw new YyghException(20001,"超过了退号截至时间");
        }
        //2.从平台请求第三方医院，通知第三方医院，该用户已取消
        HashMap<String, Object> map = new HashMap<>();
        map.put("hoscode",orderInfo.getHoscode());
        map.put("hosRecordId",orderInfo.getHosRecordId());
        JSONObject jsonObject = HttpRequestHelper.sendRequest(map, "http://localhost:9998/order/updateCancelStatus");

        //如果第三方医院不同意取消，抛出异常
        if (jsonObject == null || jsonObject.getIntValue("code") != 200) {
            throw new YyghException(20001,"取消失败");
        }

        //3.判断用户是否已支付
        if (orderInfo.getOrderStatus() == OrderStatusEnum.PAID.getStatus()) {
            //已支付，退款
            boolean flag = weiPayService.refund(orderId);
            if (!flag) {
                throw new YyghException(20001,"退款失败");
            }
        }

        //4.更新订单表的订单状态 及 支付记录表的支付状态
        orderInfo.setOrderStatus(OrderStatusEnum.CANCLE.getStatus());
        baseMapper.updateById(orderInfo);

        UpdateWrapper<PaymentInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("order_id",orderId);
        updateWrapper.set("payment_status", PaymentStatusEnum.REFUND);
        paymentInfoService.update(updateWrapper);
        //5.更新医生的剩余可预约数信息
        //发送mq信息更新预约数 我们与下单成功更新预约数使用相同的mq信息，不设置可预约数与剩余预约数，接收端可预约数减1即可
        OrderMqVo orderMqVo = new OrderMqVo();
        orderMqVo.setScheduleId(orderInfo.getScheduleId());
        //短信提示
        MsmVo msmVo = new MsmVo();
        msmVo.setPhone(orderInfo.getPatientPhone());
        orderMqVo.setMsmVo(msmVo);
        rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_ORDER,MqConst.ROUTING_ORDER,orderMqVo);
    }

    @Override
    public void patientRemind() {
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("reserve_date",new DateTime().toString("yyyy-MM-dd"));
        queryWrapper.ne("order_status",OrderStatusEnum.CANCLE.getStatus());
        List<OrderInfo> orderInfos = baseMapper.selectList(queryWrapper);
        for (OrderInfo orderInfo : orderInfos) {
            MsmVo msmVo = new MsmVo();
            msmVo.setPhone(orderInfo.getPatientPhone());
            String reserveDate = new DateTime(orderInfo.getReserveDate()).toString("yyyy-MM-dd") + (orderInfo.getReserveTime()==0 ? "上午": "下午");
            Map<String,Object> param = new HashMap<String,Object>(){{
                put("title", orderInfo.getHosname()+"|"+orderInfo.getDepname()+"|"+orderInfo.getTitle());
                put("reserveDate", reserveDate);
                put("name", orderInfo.getPatientName());
            }};
            msmVo.setParam(param);
            rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_SMS,MqConst.ROUTING_SMS,msmVo);
        }
    }

    @Override
    public Map<String, Object> getOrderStatistics(OrderCountQueryVo orderCountQueryVo) {
        List<OrderCountVo> list = baseMapper.getOrderStatistics(orderCountQueryVo);

        List<String> dateList = new ArrayList<>();
        List<Integer> countList = new ArrayList<>();
        for (OrderCountVo orderCountVo : list) {
            dateList.add(orderCountVo.getReserveDate());
            countList.add(orderCountVo.getCount());
        }
        Map<String,Object> map = new HashMap<>();
        map.put("dateList",dateList);
        map.put("countList",countList);
        return map;
    }


    private void packageOrderInfo(OrderInfo item) {
        item.getParam().put("orderStatusString",OrderStatusEnum.getStatusNameByStatus(item.getOrderStatus()));
    }


}
