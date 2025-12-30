package com.atguigu.yygh.sms.listener;

import com.atguigu.yygh.mq.MqConst;
import com.atguigu.yygh.sms.service.SmsService;
import com.atguigu.yygh.vo.msm.MsmVo;
import com.atguigu.yygh.vo.order.OrderMqVo;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Henry Guan
 * @description
 * @since 2023-05-06
 */
@Component
public class SmsMqListener {
    @Autowired
    private SmsService smsService;


    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(name = MqConst.QUEUE_SMS,durable = "true"),//创建队列
                    exchange = @Exchange(name = MqConst.EXCHANGE_DIRECT_SMS),//创建交换机
                    key = MqConst.ROUTING_SMS
            )

    })
    public void consume(OrderMqVo orderMqVo, Message message, Channel channel){
        MsmVo msmVo = orderMqVo.getMsmVo();
        //smsService.sendMessage(msmVo);
        System.out.println("给就诊人发送邮件业务代码");
    }
}
