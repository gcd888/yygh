package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author Henry Guan
 * @description
 * @since 2023-04-06
 */

public interface DepartmentService {
    void saveDepartment(Map<String, Object> requestMap);


    Page<Department> getDepartmentPage(Map<String, Object> requestMap);

    void removeDepartment(Map<String, Object> requestMap);

    List<DepartmentVo> getDeptList(String hoscode);

    String getDepName(String hoscode, String depcode);

    Department getDepartment(String hoscode, String depcode);
}