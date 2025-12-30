package com.atguigu.yygh.hosp.controller.api;

import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.utils.MD5;
import com.atguigu.yygh.hosp.bean.Result;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.hosp.utils.HttpRequestHelper;
import com.atguigu.yygh.model.hosp.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Henry Guan
 * @description 医院控制层
 * @since 2023-04-03
 */
@RestController
@RequestMapping("/api/hosp")
public class ApiHospitalController {
    @Autowired
    private HospitalService hospitalService;

    @PostMapping("/saveHospital")
    public Result saveHospital(HttpServletRequest request) {
        Map<String, Object> requestMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String requestSign = (String) requestMap.get("sign");
        String requestHoscode = (String) requestMap.get("hoscode");
        String platformSign = hospitalService.getSignByHoscode(requestHoscode);
        String encrypt = MD5.encrypt(platformSign);

        if (!StringUtils.isEmpty(platformSign) && !StringUtils.isEmpty(requestSign) && encrypt.equals(requestSign)) {
            String logoData = (String) requestMap.get("logoData");
            String result = logoData.replaceAll(" ", "+");
            requestMap.put("logoData",result);
            hospitalService.saveHosptial(requestMap);
        } else {
            throw new YyghException(20001,"保存失败");
        }
        return Result.ok();
    }

    @PostMapping("/hospital/show")
    public Result<Hospital> getHospitalInfo(HttpServletRequest request) {
        Map<String, Object> requestMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String requestSign = (String) requestMap.get("sign");
        String requestHoscode = (String) requestMap.get("hoscode");
        String platformSign = hospitalService.getSignByHoscode(requestHoscode);
        String encrypt = MD5.encrypt(platformSign);

        if (!StringUtils.isEmpty(platformSign) && !StringUtils.isEmpty(requestSign) && encrypt.equals(requestSign)) {
            Hospital hospital = hospitalService.getHospitalInfo(requestHoscode);
            return Result.ok(hospital);
        } else {
            throw new YyghException(20001,"查询失败");
        }
    }


}
