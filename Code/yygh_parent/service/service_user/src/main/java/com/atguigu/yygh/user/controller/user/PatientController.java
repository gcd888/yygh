package com.atguigu.yygh.user.controller.user;


import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.common.utils.JwtHelper;
import com.atguigu.yygh.model.user.Patient;
import com.atguigu.yygh.user.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 就诊人表 前端控制器
 * </p>
 *
 * @author Henry Guan
 * @since 2023-04-25
 */
@RestController
@RequestMapping("/user/userinfo/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping("/savePatient")
    public R savePatient(@RequestHeader String token, @RequestBody Patient patient) {
        Long userId = JwtHelper.getUserId(token);
        patient.setUserId(userId);
        patientService.save(patient);
        return R.ok();
    }

    @DeleteMapping("/delPatient/{id}")
    public R delPatient(@PathVariable Long id) {
        patientService.removeById(id);
        return R.ok();
    }

    @GetMapping("/detail/{id}")
    public R detail(@PathVariable Long id) {
        Patient patient = patientService.detail(id);
        return R.ok().data("patient",patient);
    }

    @PutMapping("/updatePatient")
    public R updatePatient(@RequestBody Patient patient) {
        patientService.updateById(patient);
        return R.ok();
    }

    @GetMapping("/findAll")
    public R findAll(@RequestHeader String token) {
        List<Patient> list = patientService.findAll(token);
        return R.ok().data("list",list);
    }

    @GetMapping("/getPatient/{id}")
    public Patient getPatient(@PathVariable("id") Long id) {
        return patientService.getById(id);
    }
}

