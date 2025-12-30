package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.utils.DateUtils;
import com.atguigu.yygh.hosp.repository.ScheduleRepository;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.model.hosp.BookingRule;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.BookingScheduleRuleVo;
import com.atguigu.yygh.vo.hosp.ScheduleOrderVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Henry Guan
 * @description 排班列表实现类
 * @since 2023-04-07
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private DepartmentService departmentService;

    @Override
    public void saveSchedule(Map<String, Object> requestMap) {
        Schedule schedule = JSONObject.parseObject(JSONObject.toJSONString(requestMap), Schedule.class);
        String hoscode = schedule.getHoscode();
        String hosScheduleId = schedule.getHosScheduleId();
        Schedule querySchedule = scheduleRepository.findByHoscodeAndHosScheduleId(hoscode,hosScheduleId);
        if (querySchedule == null) {
            schedule.setCreateTime(new Date());
            schedule.setUpdateTime(new Date());
            schedule.setIsDeleted(0);
            scheduleRepository.save(schedule);
        } else {
            schedule.setCreateTime(querySchedule.getCreateTime());
            schedule.setUpdateTime(new Date());
            schedule.setIsDeleted(querySchedule.getIsDeleted());
            schedule.setId(querySchedule.getId());
            scheduleRepository.save(schedule);
        }
    }

    @Override
    public Page<Schedule> getSchedulePage(Map<String, Object> requestMap) {
        String hoscode = (String) requestMap.get("hoscode");
        Integer page = Integer.parseInt((String) requestMap.get("page"));
        Integer limit = Integer.parseInt((String) requestMap.get("limit"));
        Pageable pageable = PageRequest.of(page-1,limit);
        Schedule schedule = new Schedule();
        schedule.setHoscode(hoscode);
        Example<Schedule> example = Example.of(schedule);
        Page<Schedule> all = scheduleRepository.findAll(example, pageable);
        return all;
    }

    @Override
    public void removeSchedule(Map<String, Object> requestMap) {
        String hoscode = (String) requestMap.get("hoscode");
        String hosScheduleId = (String) requestMap.get("hosScheduleId");
        Schedule schedule = scheduleRepository.findByHoscodeAndHosScheduleId(hoscode, hosScheduleId);
        if(schedule != null) {
            scheduleRepository.deleteById(schedule.getId());
        }
    }

    @Override
    public Map<String, Object> getScheduleDatePage(String hoscode, String depcode, Integer pageNum, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();

        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode);

        //获取当前页记录
        Aggregation aggregation = Aggregation.newAggregation(
                //1.匹配条件
                Aggregation.match(criteria),
                //2.分组字段
                Aggregation.group("workDate")
                        //查询字段
                        .first("workDate").as("workDate")
                        .count().as("docCount")
                        .sum("reservedNumber").as("reservedNumber")
                        .sum("availableNumber").as("availableNumber"),
                //3.排序
                Aggregation.sort(Sort.Direction.ASC,"workDate"),
                //4.分页
                Aggregation.skip((pageNum-1)*pageSize),
                Aggregation.limit(pageSize)
        );
        /**
         * 参数一：表示聚合条件；
         * 参数二：表示输入类型，可以根据当前指定的字节码找到mongo对应的集合
         * 参数三：表示输出类型，封装聚合后的信息
         */
        AggregationResults<BookingScheduleRuleVo> aggregate = mongoTemplate.aggregate(aggregation, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> list = aggregate.getMappedResults();
        for (BookingScheduleRuleVo bookingScheduleRuleVo : list) {
            String dayOfWeek = DateUtils.getDayOfWeek(new DateTime(bookingScheduleRuleVo.getWorkDate()));
            bookingScheduleRuleVo.setDayOfWeek(dayOfWeek);
        }
        map.put("list",list);

        //获取总记录数
        Aggregation aggregation2 = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
        );
        AggregationResults<BookingScheduleRuleVo> aggregate2 = mongoTemplate.aggregate(aggregation2, Schedule.class, BookingScheduleRuleVo.class);
        map.put("total",aggregate2.getMappedResults().size());

        //获取医院名称
        Hospital hospital = hospitalService.getHospitalInfo(hoscode);
        //其他基础数据
        Map<String, String> baseMap = new HashMap<>();
        baseMap.put("hosname",hospital.getHosname());
        map.put("baseMap",baseMap);

        return map;
    }

    @Override
    public List<Schedule> getScheduleDetail(String hoscode, String depcode, String workdate) {
        Date date = new DateTime(workdate).toDate();
        List<Schedule> list = scheduleRepository.findByHoscodeAndDepcodeAndWorkDate(hoscode,depcode,date);
        //把得到list集合遍历，向设置其他值：医院名称、科室名称、日期对应星期
        list.stream().forEach(item->{
            this.packageSchedule(item);
        });
        return list;
    }

    @Override
    public Map<String, Object> getUserSchedulePage(String hoscode, String depcode, Integer pageNum, Integer pageSize) {
        Hospital hospital = hospitalService.gethospitalDetail(hoscode);
        if (hospital == null) {
           throw new YyghException(20001,"该院信息不存在");
        }
        //获取预约规则
        BookingRule bookingRule = hospital.getBookingRule();
        //获取可预约日期分页数据
        IPage iPage = this.getListDate(pageNum, pageSize, bookingRule);
        List<Date> records = iPage.getRecords();

        //获取可预约日期科室剩余预约数
        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode).and("workDate").in(records);
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),//条件
                Aggregation.group("workDate")//分组字段
                        .first("workDate").as("workDate")
                        .count().as("docCount")
                        .sum("availableNumber").as("availableNumber")
                        .sum("reservedNumber").as("reservedNumber"),
                Aggregation.sort(Sort.Direction.ASC,"workDate")//排序
        );
        AggregationResults<BookingScheduleRuleVo> aggregate = mongoTemplate.aggregate(aggregation, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> mappedResults = aggregate.getMappedResults();
        Map<Date, BookingScheduleRuleVo> collect = mappedResults.stream().collect(Collectors.toMap(BookingScheduleRuleVo::getWorkDate, bookingScheduleRuleVo -> bookingScheduleRuleVo));

        List<BookingScheduleRuleVo> bookingScheduleRuleVoList = new ArrayList<>();
        int size = records.size();
        for (int i = 0; i < size; i++) {
            Date date = records.get(i);
            BookingScheduleRuleVo bookingScheduleRuleVo = collect.get(date);
            //当天没有排班医生
            if (bookingScheduleRuleVo == null) {
                bookingScheduleRuleVo = new BookingScheduleRuleVo();
                bookingScheduleRuleVo.setWorkDate(date);
                bookingScheduleRuleVo.setDocCount(0);
                bookingScheduleRuleVo.setReservedNumber(0);
                bookingScheduleRuleVo.setAvailableNumber(-1);

            }
            bookingScheduleRuleVo.setWorkDateMd(date);
            bookingScheduleRuleVo.setDayOfWeek(DateUtils.getDayOfWeek(new DateTime(date)));
            bookingScheduleRuleVo.setStatus(0);
            //当天预约如果过了停号时间， 不能预约
            if(i == 0 && pageNum == 1) {
                //获取该院的放号截至时间
                DateTime stopTime = this.getDateTime(new Date(), bookingRule.getStopTime());
                if(stopTime.isBeforeNow()) {
                    //停止预约
                    bookingScheduleRuleVo.setStatus(-1);
                }
            }
            //最后一页最后一条记录为即将预约   状态 0：正常 1：即将放号 -1：当天已停止挂号
            if(pageNum == iPage.getPages() && i == size-1) {
                bookingScheduleRuleVo.setStatus(1);
            }
            bookingScheduleRuleVoList.add(bookingScheduleRuleVo);
        }

        Map<String,Object> map = new HashMap<>();
        //可预约日期规则数据
        map.put("bookingScheduleList", bookingScheduleRuleVoList);
        map.put("total", iPage.getTotal());
        //其他基础数据
        Map<String, String> baseMap = new HashMap<>();
        //医院名称
        baseMap.put("hosname", hospitalService.gethospitalDetail(hoscode).getHosname());
        //科室
        Department department =departmentService.getDepartment(hoscode, depcode);
        //大科室名称
        baseMap.put("bigname", department.getBigname());
        //科室名称
        baseMap.put("depname", department.getDepname());
        //月
        baseMap.put("workDateString", new DateTime().toString("yyyy年MM月"));
        //放号时间
        baseMap.put("releaseTime", bookingRule.getReleaseTime());
        //停号时间
        baseMap.put("stopTime", bookingRule.getStopTime());
        map.put("baseMap", baseMap);
        return map;
    }

    @Override
    public Schedule getScheduleById(String scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).get();
        this.packageSchedule(schedule);
        return schedule;
    }

    @Override
    public ScheduleOrderVo getScheduleOrderVo(String scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).get();
        ScheduleOrderVo scheduleOrderVo = new ScheduleOrderVo();
        BeanUtils.copyProperties(schedule,scheduleOrderVo);
        Hospital hospital = hospitalService.gethospitalDetail(schedule.getHoscode());
        scheduleOrderVo.setHosname(hospital.getHosname());
        Department department = departmentService.getDepartment(schedule.getHoscode(), schedule.getDepcode());
        scheduleOrderVo.setDepname(department.getDepname());
        scheduleOrderVo.setReserveDate(schedule.getWorkDate());
        scheduleOrderVo.setReserveTime(schedule.getWorkTime());
        //获取退号日期
        Date quitDate = new DateTime(schedule.getWorkDate()).plusDays(hospital.getBookingRule().getQuitDay()).toDate();
        String quitTime = hospital.getBookingRule().getQuitTime();
        DateTime quitDateTime = this.getDateTime(quitDate, quitTime);
        scheduleOrderVo.setQuitTime(quitDateTime.toDate());
        //设置停止挂号时间
        DateTime stopDateTime = this.getDateTime(schedule.getWorkDate(), hospital.getBookingRule().getStopTime());
        scheduleOrderVo.setStopTime(stopDateTime.toDate());

        return scheduleOrderVo;
    }

    @Override
    public boolean updateAvailableNumber(String scheduleId, Integer availableNumber) {
        Schedule schedule = scheduleRepository.findById(scheduleId).get();
        schedule.setAvailableNumber(availableNumber);
        schedule.setUpdateTime(new Date());
        scheduleRepository.save(schedule);
        return true;
    }

    @Override
    public void cancleSchedule(String scheduleId) {
        Schedule schedule = scheduleRepository.findByHosScheduleId(scheduleId);
        schedule.setAvailableNumber(schedule.getAvailableNumber() + 1);
        scheduleRepository.save(schedule);
    }

    private IPage getListDate(Integer pageNum, Integer pageSize, BookingRule bookingRule) {
        Integer cycle = bookingRule.getCycle();
        //获取该院的放号时间
        DateTime releaseTime = this.getDateTime(new Date(),bookingRule.getReleaseTime());

        //如果过了当天预约时间，则cycle + 1
        if (releaseTime.isBeforeNow()) {
            cycle += 1;
        }

        //预约周期内所有时间列表
        List<Date> list = new ArrayList<>();
        for (int i = 0; i < cycle; i++) {
            list.add(new DateTime(new DateTime().plusDays(i).toString("yyyy-MM-dd")).toDate());
        }

        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = startIndex+pageSize > cycle ? cycle : startIndex+pageSize;

        List<Date> currentPageList = new ArrayList<>();
        for (int j = startIndex;j < endIndex;j++) {
            currentPageList.add(list.get(j));
        }
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Date> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<Date>(pageNum, pageSize, list.size());
        page.setRecords(currentPageList);
        return page;
    }

    /**
     * 根据日期和时间获取日期时间
     * @param date
     * @param releaseTime
     * @return
     */
    private DateTime getDateTime(Date date, String releaseTime) {
        String s = new DateTime(date).toString("yyyy-MM-dd") + " " + releaseTime;
        return DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").parseDateTime(s);
    }

    /**
     * 封装排班详情其他值 医院名称、科室名称、日期对应星期
     * @param schedule
     */
    private void packageSchedule(Schedule schedule) {
        //设置医院名称
        schedule.getParam().put("hosname", hospitalService.getHospitalInfo(schedule.getHoscode()).getHosname());
        //设置科室名称
        schedule.getParam().put("depname",
                departmentService.getDepName(schedule.getHoscode(), schedule.getDepcode()));
        //设置日期对应星期
        schedule.getParam().put("dayOfWeek", DateUtils.getDayOfWeek(new DateTime(schedule.getWorkDate())));
    }
}
