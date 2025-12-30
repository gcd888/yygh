package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.cmn.client.CmnFeignClient;
import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.enums.DictEnum;
import com.atguigu.yygh.hosp.mapper.HospitalSetMapper;
import com.atguigu.yygh.hosp.repository.HospitalRepository;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Henry Guan
 * @description 医院接口实现类
 * @since 2023-04-03
 */
@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private HospitalSetMapper hospitalSetMapper;

    @Autowired
    private CmnFeignClient cmnFeignClient;

    @Override
    public void saveHosptial(Map<String, Object> resultMap) {
        Hospital hospital = JSONObject.parseObject(JSONObject.toJSONString(resultMap), Hospital.class);
        String hoscode = hospital.getHoscode();
        Hospital queryHospital = hospitalRepository.findByHoscode(hoscode);
        if(null == queryHospital) {
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        } else {
            hospital.setStatus(queryHospital.getStatus());
            hospital.setCreateTime(queryHospital.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(queryHospital.getIsDeleted());
            hospital.setId(queryHospital.getId());
            hospitalRepository.save(hospital);
        }
    }

    @Override
    public String getSignByHoscode(String hoscode) {
        QueryWrapper<HospitalSet> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("hoscode",hoscode);
        HospitalSet hospitalSet = hospitalSetMapper.selectOne(queryWrapper);
        if (hospitalSet == null) {
            throw new YyghException(2001, "该院信息不存在");
        }
        return hospitalSet.getSignKey();
    }

    @Override
    public Hospital getHospitalInfo(String requestHoscode) {
        return hospitalRepository.findByHoscode(requestHoscode);
    }

    @Override
    public Page<Hospital> getHospitalPage(Integer pageNum, Integer pageSize, HospitalQueryVo hospitalQueryVo) {
        Hospital hospital = new Hospital();
        if (!StringUtils.isEmpty(hospitalQueryVo.getHosname())) {
            hospital.setHosname(hospitalQueryVo.getHosname());
        }
        if (!StringUtils.isEmpty(hospitalQueryVo.getProvinceCode())) {
            hospital.setProvinceCode(hospitalQueryVo.getProvinceCode());
        }
        if (!StringUtils.isEmpty(hospitalQueryVo.getCityCode())) {
            hospital.setCityCode(hospitalQueryVo.getCityCode());
        }
        if (!StringUtils.isEmpty(hospitalQueryVo.getDistrictCode())) {
            hospital.setDistrictCode(hospitalQueryVo.getDistrictCode());
        }
        if (!StringUtils.isEmpty(hospitalQueryVo.getHostype())) {
            hospital.setHostype(hospitalQueryVo.getHostype());
        }
        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                //.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) //改变默认字符串匹配方式：模糊查询
                .withMatcher("hosname",ExampleMatcher.GenericPropertyMatchers.contains())
                .withIgnoreCase(true); //改变默认大小写忽略方式：忽略大小写
        Example<Hospital> example = Example.of(hospital,matcher);
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by("createTime"));
        Page<Hospital> page = hospitalRepository.findAll(example, pageable);
        page.getContent().stream().forEach(item->{
           this.packageHospital(item);
        });
        return page;
    }

    @Override
    public void updateStatus(String id, Integer status) {
        if (status == 0 || status ==1) {
            Hospital hospital = hospitalRepository.findById(id).get();
            hospital.setStatus(status);
            hospital.setUpdateTime(new Date());
            hospitalRepository.save(hospital);
        }
    }

    @Override
    public Hospital getHospById(String id) {
        Hospital hospital = hospitalRepository.findById(id).get();
        packageHospital(hospital);
        return hospital;
    }

    @Override
    public List<Hospital> findByNameLike(String name) {
        return hospitalRepository.findByHosnameLike(name);
    }

    @Override
    public Hospital gethospitalDetail(String hoscode) {
        Hospital hospital = hospitalRepository.findByHoscode(hoscode);
        this.packageHospital(hospital);
        return hospital;
    }

    private void packageHospital(Hospital hospital) {
        String hostype = hospital.getHostype();
        String provinceCode = hospital.getProvinceCode();
        String cityCode = hospital.getCityCode();
        String districtCode = hospital.getDistrictCode();

        String provinceAdress = cmnFeignClient.getNameByValue(Long.parseLong(provinceCode));
        String cicyName = cmnFeignClient.getNameByValue(Long.parseLong(cityCode));
        String districtAddress = cmnFeignClient.getNameByValue(Long.parseLong(districtCode));

        String level = cmnFeignClient.getNameByDictCodeAndValue(DictEnum.HOSTYPE.getDictCode(), Long.parseLong(hostype));
        hospital.getParam().put("hostypeString",level);
        hospital.getParam().put("fullAddress",provinceAdress+cicyName+districtAddress+hospital.getAddress());
    }
}
