package com.atguigu.yygh.hosp.controller.user;

import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.ScheduleOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author Henry Guan
 * @description 排班信息
 * @since 2023-04-28
 */

@RestController
@RequestMapping("/user/hosp/schedule")
public class UserScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/getUserSchedulePage/{hoscode}/{depcode}/{pageNum}/{pageSize}")
    public R getUserSchedulePage(@PathVariable String hoscode,@PathVariable String depcode,
                                 @PathVariable Integer pageNum,@PathVariable Integer pageSize) {
        Map<String,Object> map = scheduleService.getUserSchedulePage(hoscode,depcode,pageNum,pageSize);
        return R.ok().data(map);
    }

    @GetMapping("/getUserScheduleDetail/{hoscode}/{depcode}/{workDate}")
    public R getUserScheduleDetail(@PathVariable String hoscode,@PathVariable String depcode,@PathVariable String workDate) {
        List<Schedule> list = scheduleService.getScheduleDetail(hoscode, depcode, workDate);
        return R.ok().data("list",list);
    }

    @GetMapping("/getScheduleById/{scheduleId}")
    public R getScheduleById(@PathVariable String scheduleId) {
        Schedule schedule = scheduleService.getScheduleById(scheduleId);
        return R.ok().data("schedule",schedule);
    }

    @GetMapping("/getScheduleOrderVo/{scheduleId}")
    public ScheduleOrderVo getScheduleOrderVo(@PathVariable("scheduleId") String scheduleId) {
        return scheduleService.getScheduleOrderVo(scheduleId);
    }
}
