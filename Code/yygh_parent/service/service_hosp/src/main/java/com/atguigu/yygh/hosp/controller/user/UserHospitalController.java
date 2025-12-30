package com.atguigu.yygh.hosp.controller.user;

import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Henry Guan
 * @description 用户医院控制类
 * @since 2023-04-17
 */
@RestController
@RequestMapping("user/hosp/hospital")
public class UserHospitalController {

    @Autowired
    private HospitalService hospitalService;

    /**
     * 获取医院列表
     * @param hospitalQueryVo
     * @return
     */
    @GetMapping("/list")
    public R getHospitalList(HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> hospitalPage = hospitalService.getHospitalPage(1, 1000000, hospitalQueryVo);
        return R.ok().data("list",hospitalPage.getContent());
    }

    /**
     * 根据医院名称获取医院信息
     * @param name
     * @return
     */
    @GetMapping("/{name}")
    public R findByName(@PathVariable String name) {
       List<Hospital> hospitals =  hospitalService.findByNameLike(name);
       return R.ok().data("list",hospitals);
    }

    /**
     * 根据医院编号获取医院详情信息
     * @param hoscode
     * @return
     */
    @GetMapping("/detail/{hoscode}")
    public R getHospitalDetail(@PathVariable String hoscode) {
        Hospital hospital = hospitalService.gethospitalDetail(hoscode);
        return R.ok().data("hospital",hospital);
    }

}
