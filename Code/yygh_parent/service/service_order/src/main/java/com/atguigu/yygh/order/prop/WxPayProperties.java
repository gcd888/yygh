package com.atguigu.yygh.order.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author Henry Guan
 * @description
 * @since 2023-05-09
 */
@ConfigurationProperties(prefix = "wxpay")//决定了读取properies文件中以谁开头的配置项
@PropertySource(value = "classpath:wxpay.properties")
@Component
@Data
public class WxPayProperties {
    //公众账号ID
    private String appid;
    //商户号
    private String partner;
    //商户key
    private String partnerkey;
}
