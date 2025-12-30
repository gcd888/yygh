package com.atguigu.yygh.cmn.service;

import com.atguigu.yygh.model.cmn.Dict;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 组织架构表 服务类
 * </p>
 *
 * @author Admin
 * @since 2023-03-23
 */
public interface DictService extends IService<Dict> {

    /**
     * 根据父Id查询数据字典
     * @param pid
     * @return
     */
    List<Dict> getChildListByPid(Integer pid);

    /**
     * 下载数据字典
     * @param response
     * @throws IOException
     */
    void download(HttpServletResponse response) throws IOException;

    void upload(MultipartFile file) throws IOException;

    String getNameByValue(Long value);

    String getNameByDictCodeAndValue(String dictCode, Long value);
}
