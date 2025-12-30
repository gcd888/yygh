package com.atguigu.yygh.hosp.controller.admin;

import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.model.hosp.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author Henry Guan
 * @description 排班信息控制类
 * @since 2023-04-14
 */
@RestController
@RequestMapping("/admin/hosp/schedule")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/{hoscode}/{depcode}/{pageNum}/{pageSize}")
    public R getScheduleDatePage(@PathVariable String hoscode,@PathVariable String depcode,
                                 @PathVariable Integer pageNum,@PathVariable Integer pageSize){
        Map<String,Object> map = scheduleService.getScheduleDatePage(hoscode,depcode,pageNum,pageSize);
        return R.ok().data(map);
    }
    @GetMapping("/{hoscode}/{depcode}/{workdate}")
    public R getScheduleDetail(@PathVariable String hoscode,@PathVariable String depcode,@PathVariable String workdate){
        List<Schedule> list = scheduleService.getScheduleDetail(hoscode,depcode,workdate);
        return R.ok().data("list",list);
    }
}
