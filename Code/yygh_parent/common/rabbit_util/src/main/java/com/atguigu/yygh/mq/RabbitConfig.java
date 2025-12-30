package com.atguigu.yygh.mq;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author Henry Guan
 * @description
 * @since 2023-05-06
 */
@SpringBootConfiguration
public class RabbitConfig {
    //作用：生产者端就是将发送到rabbitmq中的pojo对象自动转换为json格式存储；从rabbitmq中消费消息时，自动把json格式的字母转换为pojo对象
    @Bean
    public MessageConverter getMessageConvert(){
        return new Jackson2JsonMessageConverter();
    }
}
