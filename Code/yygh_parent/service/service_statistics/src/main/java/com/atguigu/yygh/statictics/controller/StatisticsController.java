package com.atguigu.yygh.statictics.controller;

import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.statictics.service.StatisticsService;
import com.atguigu.yygh.vo.order.OrderCountQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Henry Guan
 * @description 统计数据
 * @since 2023-05-11
 */
@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/StatisticsOrder")
    public R StatisticsOrder(OrderCountQueryVo orderCountQueryVo){
        Map<String,Object> map = statisticsService.StatisticsOrder(orderCountQueryVo);
        return R.ok().data("map",map);
    }
}
