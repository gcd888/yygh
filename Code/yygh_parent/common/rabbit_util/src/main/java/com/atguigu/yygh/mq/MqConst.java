package com.atguigu.yygh.mq;

public class MqConst {
    /**
     * 预约下单
     */
    //交换机
    public static final String EXCHANGE_DIRECT_ORDER = "exchange.direct.order";
    //路由
    public static final String ROUTING_ORDER = "order";
    //队列
    public static final String QUEUE_ORDER  = "queue.order";

    /**
     * 短信
     */
    public static final String EXCHANGE_DIRECT_SMS = "exchange.direct.sms";
    public static final String ROUTING_SMS = "sms";
    public static final String QUEUE_SMS = "queue.sms";

    /**
     * 定时任务：每天上午6点执行
     */
    public static final String EXCHANGE_DIRECT_TASK = "exchange.direct.task";
    public static final String ROUTING_TASK_6 = "task_6";
    public static final String QUEUE_TASK_6 = "queue.task.6";
}
