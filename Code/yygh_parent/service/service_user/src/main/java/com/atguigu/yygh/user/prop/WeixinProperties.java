package com.atguigu.yygh.user.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Henry Guan
 * @description 微信
 * @since 2023-04-20
 */

@ConfigurationProperties(prefix = "weixin")
@Data
//@Component+@Value("${weixin.appid}")
//@Component+@ConfigurationProperties(prefix = "weixin")
//@ConfigurationProperties(prefix = "weixin")+@EnableConfigurationProperties(value = WeixinProperties.class)
public class WeixinProperties {
    private String appid;
    private String appsecret;
    private String redirecturl;
}
