package com.atguigu.yygh.sms.service;

/**
 * @author Henry Guan
 * @description
 * @since 2023-04-19
 */

public interface SmsService {
    boolean sendCode(String phone);
}
