package com.atguigu.yygh.order.controller;

import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.order.service.OrderInfoService;
import com.atguigu.yygh.order.service.PaymentInfoService;
import com.atguigu.yygh.order.service.WeiPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Henry Guan
 * @description 微信支付
 * @since 2023-05-08
 */
@RestController
@RequestMapping("/user/order/weipay")
public class WeiPayController {

    @Autowired
    private WeiPayService weiPayService;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private PaymentInfoService paymentInfoService;



    @GetMapping("/createNative/{orderId}")
    public R createNative(@PathVariable Long orderId) {
        String url = weiPayService.createNative(orderId);
        return R.ok().data("url",url);
    }

    @GetMapping("/queryPayStatus/{orderId}")
    public R queryPayStatus(@PathVariable Long orderId) {
        Map<String,String> resultMap = weiPayService.queryPayStatus(orderId);

        if (resultMap == null) {
            return R.error().message("查询支付状态出错");
        }
        if ("SUCCESS".equals(resultMap.get("trade_state"))) {//如果成功
            weiPayService.paySuccess(orderId,resultMap);
            return R.ok();
        }
        return R.ok().message("支付中");

    }

}
