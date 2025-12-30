package com.atguigu.yygh.user.service.impl;


import com.atguigu.yygh.cmn.client.CmnFeignClient;
import com.atguigu.yygh.common.utils.JwtHelper;
import com.atguigu.yygh.model.user.Patient;
import com.atguigu.yygh.user.mapper.PatientMapper;
import com.atguigu.yygh.user.service.PatientService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 就诊人表 服务实现类
 * </p>
 *
 * @author Henry Guan
 * @since 2023-04-25
 */
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {

    @Autowired
    private CmnFeignClient cmnFeignClient;

    @Override
    public List<Patient> findAll(String token) {
        Long userId = JwtHelper.getUserId(token);
        return getPatients(userId);
    }

    @Override
    public Patient detail(Long id) {
        Patient patient = baseMapper.selectById(id);
        this.packagePatient(patient);
        return patient;
    }

    @Override
    public List<Patient> getByUserId(Long userId) {
        return getPatients(userId);
    }

    private List<Patient> getPatients(Long userId) {
        QueryWrapper<Patient> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        List<Patient> patients = baseMapper.selectList(wrapper);
        for (Patient patient : patients) {
            this.packagePatient(patient);
        }
        return patients;
    }

    private void packagePatient(Patient patient) {
        String provinceString = cmnFeignClient.getNameByValue(Long.valueOf(patient.getProvinceCode()));
        String cityString = cmnFeignClient.getNameByValue(Long.valueOf(patient.getCityCode()));
        String districtString = cmnFeignClient.getNameByValue(Long.valueOf(patient.getDistrictCode()));
        patient.getParam().put("certificatesTypeString", cmnFeignClient.getNameByValue(Long.valueOf(patient.getContactsCertificatesType())));
        patient.getParam().put("provinceString",provinceString);
        patient.getParam().put("cityString",cityString);
        patient.getParam().put("districtString",districtString);
        patient.getParam().put("fullAddress",provinceString+cityString+districtString+patient.getAddress());
    }
}
