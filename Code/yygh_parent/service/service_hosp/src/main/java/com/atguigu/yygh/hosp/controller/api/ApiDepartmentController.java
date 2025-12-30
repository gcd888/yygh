package com.atguigu.yygh.hosp.controller.api;

import com.atguigu.yygh.hosp.bean.Result;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.hosp.utils.HttpRequestHelper;
import com.atguigu.yygh.model.hosp.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Henry Guan
 * @description 科室列表
 * @since 2023-04-06
 */
@RestController
@RequestMapping("/api/hosp")
public class ApiDepartmentController {
    @Autowired
    private DepartmentService departmentService;

    /**
     * 科室列表添加
     * @param request
     * @return
     */
    @PostMapping("/saveDepartment")
    public Result saveDepartment(HttpServletRequest request) {
        Map<String, Object> requestMap = HttpRequestHelper.switchMap(request.getParameterMap());
        departmentService.saveDepartment(requestMap);
        return Result.ok();
    }

    /**
     * 科室列表的分页查询
     * @param request
     * @return
     */
    @PostMapping("/department/list")
    public Result<Page> getDepartmentPage(HttpServletRequest request) {
        Map<String, Object> requestMap = HttpRequestHelper.switchMap(request.getParameterMap());
        Page<Department> page = departmentService.getDepartmentPage(requestMap);
        return Result.ok(page);
    }

    /**
     * 删除科室信息
     * @param request
     * @return
     */
    @PostMapping("/department/remove")
    public Result removeDepartment(HttpServletRequest request) {
        Map<String, Object> requestMap = HttpRequestHelper.switchMap(request.getParameterMap());
        departmentService.removeDepartment(requestMap);
        return Result.ok();
    }
}
