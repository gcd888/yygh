package com.atguigu.yygh.statictics.service;

import com.atguigu.yygh.vo.order.OrderCountQueryVo;

import java.util.Map;

/**
 * @author Henry Guan
 * @description
 * @since 2023-05-11
 */

public interface StatisticsService {
    Map<String, Object> StatisticsOrder(OrderCountQueryVo orderCountQueryVo);
}
