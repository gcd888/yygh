package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author Henry Guan
 * @description 医院接口
 * @since 2023-04-03
 */

public interface HospitalService {
    void saveHosptial(Map<String, Object> resultMap);

    String getSignByHoscode(String hoscode);

    Hospital getHospitalInfo(String requestHoscode);

    Page<Hospital> getHospitalPage(Integer pageNum, Integer pageSize, HospitalQueryVo hospitalQueryVo);

    void updateStatus(String id, Integer status);

    Hospital getHospById(String id);

    List<Hospital> findByNameLike(String name);

    Hospital gethospitalDetail(String hoscode);
}
