package com.atguigu.yygh.cmn.controller;


import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.model.cmn.Dict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 组织架构表 前端控制器
 * </p>
 *
 * @author Admin
 * @since 2023-03-23
 */
@RestController
@RequestMapping("/admin/cmn")
public class DictController {

    @Autowired
    private DictService dictService;

    /**
     *
     * @param pid
     * @return
     */
    @GetMapping(value = "/childList/{pid}")
    public R getChildListByPid(@PathVariable Integer pid) {
        List<Dict> list = dictService.getChildListByPid(pid);
        return R.ok().data("items",list);
    }

    @GetMapping(value = "/download")
    public void download(HttpServletResponse response) throws IOException {
        dictService.download(response);
    }

    @PostMapping(value = "/upload")
    public R upload(MultipartFile file) throws IOException {
        dictService.upload(file);
        return R.ok();
    }

    /**
     * 根据值去dict表获取名称
     * @param value
     * @return
     */
    @GetMapping("/{value}")
    public String getNameByValue(@PathVariable("value") Long value) {
        return dictService.getNameByValue(value);
    }

    /**
     * 根据值和字典编码去dict表获取名称
     * @param dictCode
     * @param value
     * @return
     */
    @GetMapping("/{dictCode}/{value}")
    public String getNameByDictCodeAndValue(@PathVariable("dictCode") String dictCode, @PathVariable("value") Long value) {
        return dictService.getNameByDictCodeAndValue(dictCode,value);
    }
}

