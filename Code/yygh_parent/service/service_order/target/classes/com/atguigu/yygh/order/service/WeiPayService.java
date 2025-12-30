package com.atguigu.yygh.order.service;

import java.util.Map;

/**
 * @author Henry Guan
 * @description
 * @since 2023-05-08
 */

public interface WeiPayService {
    String createNative(Long orderId);

    Map<String, String> queryPayStatus(Long orderId);

    void paySuccess(Long orderId, Map<String, String> resultMap);

    boolean refund(Long orderId);
}
