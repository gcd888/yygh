package com.atguigu.yygh.hosp.controller.admin;

import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * @author Henry Guan
 * @description 医院管理控制层
 * @since 2023-04-07
 */
@RestController
@RequestMapping("/admin/hospital")
public class HospitalController {
    @Autowired
    private HospitalService hospitalService;

    /**
     * 分页查询所有医院信息
     * @param pageNum
     * @param pageSize
     * @param hospitalQueryVo
     * @return
     */
    @GetMapping("/{pageNum}/{pageSize}")
    public R getHospitalPage(@PathVariable Integer pageNum,@PathVariable Integer pageSize,HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> page = hospitalService.getHospitalPage(pageNum,pageSize,hospitalQueryVo);
        return R.ok().data("total",page.getTotalElements()).data("list",page.getContent());
    }

    /**
     * 设置医院上线/下线
     * @param id
     * @param status
     * @return
     */
    @PutMapping("/updateStatus/{id}/{status}")
    public R updateStatus(@PathVariable String id,@PathVariable Integer status) {
        hospitalService.updateStatus(id,status);
        return R.ok();
    }

    /**
     * 查看医院明细
     * @param id
     * @return
     */
    @GetMapping("/getHospById/{id}")
    public R getHospById(@PathVariable String id) {
        Hospital hospital = hospitalService.getHospById(id);
        return R.ok().data("hospital",hospital);
    }
}
