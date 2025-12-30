package com.atguigu.yygh.task.job;

import com.atguigu.yygh.mq.MqConst;
import com.atguigu.yygh.mq.RabbitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Henry Guan
 * @description 就诊提醒定时任务
 * @since 2023-05-10
 */
@Component
@EnableScheduling
public class PatientRemindJob {

    @Autowired
    private RabbitService rabbitService;

    @Scheduled(cron = "0 0 6 * * ?") //每天6点执行此任务
    public void sendPatientRemindSms() {

        rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_TASK,MqConst.ROUTING_TASK_6," ");
    }
}
