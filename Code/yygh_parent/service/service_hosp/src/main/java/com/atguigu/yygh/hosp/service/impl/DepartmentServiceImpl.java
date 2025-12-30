package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.hosp.repository.DepartmentRepositoty;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.vo.hosp.DepartmentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Henry Guan
 * @description 科室列表实现类
 * @since 2023-04-06
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepositoty departmentRepositoty;

    @Override
    public void saveDepartment(Map<String, Object> requestMap) {
        Department department = JSONObject.parseObject(JSONObject.toJSONString(requestMap), Department.class);
        String hoscode = department.getHoscode();
        String depcode = department.getDepcode();
        Department queryDepartment = departmentRepositoty.findByHoscodeAndDepcode(hoscode,depcode);
        if (queryDepartment == null) {
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepositoty.save(department);
        } else {
            department.setCreateTime(queryDepartment.getCreateTime());
            department.setUpdateTime(new Date());
            department.setIsDeleted(queryDepartment.getIsDeleted());
            department.setId(queryDepartment.getId());
            departmentRepositoty.save(department);
        }
    }

    @Override
    public Page<Department> getDepartmentPage(Map<String, Object> requestMap) {
        Integer page = Integer.parseInt((String) requestMap.get("page"));
        Integer limit = Integer.parseInt((String) requestMap.get("limit"));
        String hoscode = (String) requestMap.get("hoscode");
        Department department = new Department();
        department.setHoscode(hoscode);
        Example<Department> example = Example.of(department);
        Sort sort = Sort.by(Sort.Direction.ASC,"decode");
        Pageable pageable = PageRequest.of(page-1,limit,sort);
        Page<Department> all = departmentRepositoty.findAll(example, pageable);
        return all;
    }

    @Override
    public void removeDepartment(Map<String, Object> requestMap) {
        String hoscode = (String) requestMap.get("hoscode");
        String depcode = (String) requestMap.get("depcode");
        Department department = departmentRepositoty.findByHoscodeAndDepcode(hoscode, depcode);
        if (department != null) {
            departmentRepositoty.deleteById(department.getId());
        }
    }

    @Override
    public List<DepartmentVo> getDeptList(String hoscode) {
        Department department = new Department();
        department.setHoscode(hoscode);
        Example<Department> example = Example.of(department);
        //获取该院所有科室
        List<Department> all = departmentRepositoty.findAll(example);
        //获取该大科室所有科室信息
        Map<String, List<Department>> collect = all.stream().collect(Collectors.groupingBy(Department::getBigcode));

       List<DepartmentVo> bigDepartmentList = new ArrayList<>();
        for (Map.Entry<String, List<Department>> entry : collect.entrySet()) {
            DepartmentVo bigDepartmentVo = new DepartmentVo();
            String bigcode = entry.getKey();
            List<Department> values = entry.getValue();

            List<DepartmentVo> ChildDepartmentList = new ArrayList<>();
            for (Department value : values) {
                DepartmentVo childDepartmentVo = new DepartmentVo();
                childDepartmentVo.setDepcode(value.getDepcode());
                childDepartmentVo.setDepname(value.getDepname());
                ChildDepartmentList.add(childDepartmentVo);
            }
            bigDepartmentVo.setDepcode(bigcode);
            bigDepartmentVo.setDepname(values.get(0).getDepname());
            bigDepartmentVo.setChildren(ChildDepartmentList);
            bigDepartmentList.add(bigDepartmentVo);
        }
        return bigDepartmentList;
    }

    @Override
    public String getDepName(String hoscode, String depcode) {
        Department department = departmentRepositoty.findByHoscodeAndDepcode(hoscode, depcode);
        if (department != null) {
            return department.getDepname();
        }
        return null;
    }

    @Override
    public Department getDepartment(String hoscode, String depcode) {
        return departmentRepositoty.findByHoscodeAndDepcode(hoscode, depcode);
    }


}
