package com.atguigu.yygh.cmn.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Henry Guan
 * @description 字典模块自定义feign客户端接口
 * @since 2023-04-11
 */
@FeignClient(value = "service-cmn")
public interface CmnFeignClient {

    /**
     * 根据值去dict表获取名称
     * @param value
     * @return
     */
    @GetMapping("/admin/cmn/{value}")
    public String getNameByValue(@PathVariable("value") Long value);

    /**
     * 根据值和字典编码去dict表获取名称
     * @param dictCode
     * @param value
     * @return
     */
    @GetMapping("/admin/cmn/{dictCode}/{value}")
    public String getNameByDictCodeAndValue(@PathVariable("dictCode") String dictCode, @PathVariable("value") Long value);

}
