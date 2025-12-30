package com.atguigu.yygh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Henry Guan
 * @description 对象存储服务主启动类
 * @since 2023-04-24
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = {"com.atguigu"})
public class ServiceOssAppliaction {
    public static void main(String[] args) {
        SpringApplication.run(ServiceOssAppliaction.class,args);
    }
}
