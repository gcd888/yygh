package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.ScheduleOrderVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author Henry Guan
 * @description
 * @since 2023-04-07
 */

public interface ScheduleService {
    void saveSchedule(Map<String, Object> requestMap);

    Page<Schedule> getSchedulePage(Map<String, Object> requestMap);

    void removeSchedule(Map<String, Object> requestMap);

    Map<String, Object> getScheduleDatePage(String hoscode, String depcode, Integer pageNum, Integer pageSize);

    List<Schedule> getScheduleDetail(String hoscode, String depcode, String workdate);

    Map<String, Object> getUserSchedulePage(String hoscode, String depcode, Integer pageNum, Integer pageSize);

    Schedule getScheduleById(String scheduleId);

    ScheduleOrderVo getScheduleOrderVo(String scheduleId);

    boolean updateAvailableNumber(String scheduleId, Integer availableNumber);

    void cancleSchedule(String scheduleId);
}
