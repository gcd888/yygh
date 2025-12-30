package com.atguigu.yygh.order.service;


import com.atguigu.yygh.model.order.OrderInfo;
import com.atguigu.yygh.model.order.PaymentInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 支付信息表 服务类
 * </p>
 *
 * @author Henry Guan
 * @since 2023-05-08
 */
public interface PaymentInfoService extends IService<PaymentInfo> {

    void savePaymentInfo(OrderInfo orderInfo, Integer paymentType);
}
