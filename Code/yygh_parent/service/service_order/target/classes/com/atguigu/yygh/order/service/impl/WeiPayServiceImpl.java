package com.atguigu.yygh.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.enums.OrderStatusEnum;
import com.atguigu.yygh.enums.PaymentStatusEnum;
import com.atguigu.yygh.enums.PaymentTypeEnum;
import com.atguigu.yygh.enums.RefundStatusEnum;
import com.atguigu.yygh.model.order.OrderInfo;
import com.atguigu.yygh.model.order.PaymentInfo;
import com.atguigu.yygh.model.order.RefundInfo;
import com.atguigu.yygh.order.prop.WxPayProperties;
import com.atguigu.yygh.order.service.OrderInfoService;
import com.atguigu.yygh.order.service.PaymentInfoService;
import com.atguigu.yygh.order.service.RefundInfoService;
import com.atguigu.yygh.order.service.WeiPayService;
import com.atguigu.yygh.order.utils.HttpClient;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Henry Guan
 * @description
 * @since 2023-05-08
 */
@Service
public class WeiPayServiceImpl implements WeiPayService {

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private PaymentInfoService paymentInfoService;

    @Autowired
    private WxPayProperties wxPayProperties;

    @Autowired
    private RefundInfoService refundInfoService;

    @Override
    public String createNative(Long orderId) {
        //1.根据订单id去数据库获取订单信息
        OrderInfo orderInfo = orderInfoService.getById(orderId);
        //2.保存支付记录
        paymentInfoService.savePaymentInfo(orderInfo, PaymentTypeEnum.WEIXIN.getStatus());
        //3.请求微信服务器获取微信支付的url地址
        HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("appid",wxPayProperties.getAppid());//公众账号ID
        paramMap.put("mch_id",wxPayProperties.getPartner());//商户号
        paramMap.put("nonce_str",WXPayUtil.generateNonceStr());//随机字符串
        paramMap.put("sign","");//签名
        Date reserveDate = orderInfo.getReserveDate();
        String reserveDateString = new DateTime(reserveDate).toString("yyyy/MM/dd");
        String body = reserveDateString + "就诊"+ orderInfo.getDepname();
        paramMap.put("body", body);//商品描述
        paramMap.put("out_trade_no",orderInfo.getOutTradeNo());//商户订单号
        //paramMap.put("total_fee",orderInfo.getAmount().multiply(new BigDecimal("100")).toString());
        paramMap.put("total_fee","1");//订单总金额，单位为分
        paramMap.put("spbill_create_ip","127.0.0.1");//终端IP
        paramMap.put("notify_url","http://guli.shop/api/order/weixinPay/weixinNotify");//通知地址
        paramMap.put("trade_type","NATIVE");//交易类型

        try {
            String xml = WXPayUtil.generateSignedXml(paramMap, wxPayProperties.getPartnerkey());
            httpClient.setXmlParam(xml);
            httpClient.setHttps(true);//支持https协议
            httpClient.post();//发送请求
            String content = httpClient.getContent();//获取返回结果
            Map<String, String> map = WXPayUtil.xmlToMap(content);
            //4.将url返回给前端
            if ("SUCCESS".equals(map.get("return_code")) && "SUCCESS".equals(map.get("result_code"))) {
                return map.get("code_url");
            }
        } catch (Exception e) {
            throw new YyghException(20001,"微信支付调用失败");
        }
        return "";
    }

    @Override
    public Map<String, String> queryPayStatus(Long orderId) {
        OrderInfo orderInfo = orderInfoService.getById(orderId);

        HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
        Map<String,String> map = new HashMap<>();
        map.put("appid",wxPayProperties.getAppid());//公众账号ID
        map.put("mch_id",wxPayProperties.getPartner());//商户号
        map.put("out_trade_no",orderInfo.getOutTradeNo());//商户订单号
        map.put("nonce_str",WXPayUtil.generateNonceStr());//随机字符串
        map.put("sign","");//签名

        try {
            String xml = WXPayUtil.generateSignedXml(map,wxPayProperties.getPartnerkey());
            httpClient.setHttps(true);
            httpClient.setXmlParam(xml);
            httpClient.post();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(httpClient.getContent());
            return resultMap;
        } catch (Exception e) {
            return null;
        }

    }

    @Transactional
    @Override
    public void paySuccess(Long orderId, Map<String, String> resultMap) {
        //更改订单表的订单状态
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setId(orderId);
        orderInfo.setOrderStatus(OrderStatusEnum.PAID.getStatus());
        orderInfoService.updateById(orderInfo);
        // 更新支付记录表的支付状态
        UpdateWrapper<PaymentInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("order_id",orderId);
        updateWrapper.set("trade_no",resultMap.get("transaction_id"));
        updateWrapper.set("payment_status", PaymentStatusEnum.PAID.getStatus());
        updateWrapper.set("callback_time",new Date());
        updateWrapper.set("callback_content", JSONObject.toJSONString(resultMap));
        paymentInfoService.update(updateWrapper);
    }

    @Override
    public boolean refund(Long orderId) {
        QueryWrapper<PaymentInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id",orderId);
        PaymentInfo paymentInfo = paymentInfoService.getOne(queryWrapper);
        RefundInfo refundInfo = refundInfoService.saveRefundInfo(paymentInfo);

        //已退款
        if (refundInfo.getRefundStatus().intValue() == RefundStatusEnum.REFUND.getStatus()) {
            return true;
        }

        //执行微信退款
        HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/secapi/pay/refund");
        Map<String,String> paramMap = new HashMap<>(8);
        paramMap.put("appid",wxPayProperties.getAppid());       //公众账号ID
        paramMap.put("mch_id",wxPayProperties.getPartner());   //商户编号
        paramMap.put("nonce_str",WXPayUtil.generateNonceStr()); //随机字符串
        paramMap.put("sign",""); //签名
        paramMap.put("transaction_id",refundInfo.getTradeNo()); //微信订单号
        paramMap.put("out_trade_no",refundInfo.getOutTradeNo()); //商户订单编号
        paramMap.put("out_refund_no","tk"+refundInfo.getOutTradeNo()); //商户退款单号
        //paramMap.put("total_fee",paymentInfoQuery.getTotalAmount().multiply(new BigDecimal("100")).longValue()+"");
        //paramMap.put("refund_fee",paymentInfoQuery.getTotalAmount().multiply(new BigDecimal("100")).longValue()+"");
        paramMap.put("total_fee","1"); //订单金额
        paramMap.put("refund_fee","1"); //退款金额
        try {
            String paramXml = WXPayUtil.generateSignedXml(paramMap,wxPayProperties.getPartnerkey());
            httpClient.setXmlParam(paramXml);
            httpClient.setHttps(true);
            httpClient.setCert(true);//设置证书支持
            httpClient.setCertPassword(wxPayProperties.getPartner());//设置证书密码
            httpClient.post();

            //3、返回第三方的数据
            String xml = httpClient.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            if (null != resultMap && WXPayConstants.SUCCESS.equalsIgnoreCase(resultMap.get("result_code"))) {
                refundInfo.setCallbackTime(new Date());
                refundInfo.setTradeNo(resultMap.get("refund_id"));
                refundInfo.setRefundStatus(RefundStatusEnum.REFUND.getStatus());
                refundInfo.setCallbackContent(JSONObject.toJSONString(resultMap));
                refundInfoService.updateById(refundInfo);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
