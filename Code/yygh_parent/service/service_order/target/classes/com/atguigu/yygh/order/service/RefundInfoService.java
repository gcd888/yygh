package com.atguigu.yygh.order.service;


import com.atguigu.yygh.model.order.PaymentInfo;
import com.atguigu.yygh.model.order.RefundInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 退款信息表 服务类
 * </p>
 *
 * @author Henry Guan
 * @since 2023-05-08
 */
public interface RefundInfoService extends IService<RefundInfo> {

    RefundInfo saveRefundInfo(PaymentInfo paymentInfo);
}
