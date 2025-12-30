package com.atguigu.yygh.oss.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author Henry Guan
 * @description 获取配置信息
 * @since 2023-04-24
 */
@ConfigurationProperties(prefix = "oss.file")
@PropertySource(value = {"classpath:oss.properties"})//1.@PropertySource不支持yml文件2.不能同时和@EnableConfigurationProperties同时使用
@Component
@Data
public class OssProperties {
    /*
    方式一：@Value(“${oss.file.endpoint}”) + 类上@Component
    方式二：@ConfigurationProperties(prefix = "oss.file") + 主启动类加@EnableConfigurationProperties(value = OssProperties.class)
    方式三：@ConfigurationProperties(prefix = "oss.file") + @Component + @PropertySource(value = {"classpath:oss.properties"})
     */

    private String endpoint;

    private String keyId;

    private String keySecret;

    private String bucketName;
}
