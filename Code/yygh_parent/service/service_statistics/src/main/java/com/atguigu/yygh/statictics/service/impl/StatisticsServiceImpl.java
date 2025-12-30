package com.atguigu.yygh.statictics.service.impl;

import com.atguigu.yygh.order.client.OrderInfoFeignClient;
import com.atguigu.yygh.statictics.service.StatisticsService;
import com.atguigu.yygh.vo.order.OrderCountQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Henry Guan
 * @description
 * @since 2023-05-11
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private OrderInfoFeignClient orderInfoFeignClient;

    @Override
    public Map<String, Object> StatisticsOrder(OrderCountQueryVo orderCountQueryVo) {
        return orderInfoFeignClient.getOrderStatistics(orderCountQueryVo);
    }
}
