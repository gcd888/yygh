package com.atguigu.yygh.user;

import com.atguigu.yygh.user.prop.WeixinProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Henry Guan
 * @description 用户主启动类
 * @since 2023-04-19
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.atguigu") //扫描当前模块和common下包
@EnableDiscoveryClient //nacos注册
@EnableFeignClients(basePackages = "com.atguigu") //feign远程调用
@MapperScan("com.atguigu.yygh.user.mapper")
@EnableConfigurationProperties(value = WeixinProperties.class)
public class ServiceUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceUserApplication.class,args);
    }
}
