package com.atguigu.yygh.order.client;

import com.atguigu.yygh.vo.order.OrderCountQueryVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @author Henry Guan
 * @description
 * @since 2023-05-11
 */

@FeignClient(value = "service-order")
public interface OrderInfoFeignClient {

    @PostMapping("/user/order/orderInfo/getOrderStatistics")
    public Map<String,Object> getOrderStatistics(@RequestBody OrderCountQueryVo orderCountQueryVo);
}
