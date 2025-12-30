package com.atguigu.yygh.hosp.controller.api;

import com.atguigu.yygh.hosp.bean.Result;
import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.hosp.utils.HttpRequestHelper;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.ScheduleOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Henry Guan
 * @description 排班列表控制层
 * @since 2023-04-07
 */
@RestController
@RequestMapping("/api/hosp")
public class ApiScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("/saveSchedule")
    public Result saveSchedule(HttpServletRequest request) {
        Map<String, Object> requestMap = HttpRequestHelper.switchMap(request.getParameterMap());
        scheduleService.saveSchedule(requestMap);
        return Result.ok();
    }

    @PostMapping("/schedule/list")
    public Result<Page> getSchedulePage(HttpServletRequest request) {
        Map<String, Object> requestMap = HttpRequestHelper.switchMap(request.getParameterMap());
        Page<Schedule> page = scheduleService.getSchedulePage(requestMap);
        return Result.ok(page);
    }

    @PostMapping("/schedule/remove")
    public Result removeSchedule(HttpServletRequest request) {
        Map<String, Object> requestMap = HttpRequestHelper.switchMap(request.getParameterMap());
        scheduleService.removeSchedule(requestMap);
        return Result.ok();
    }

}
