package com.atguigu.yygh.hosp.controller.admin;


import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.common.utils.MD5;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

/**
 * <p>
 * 医院设置表 前端控制器
 * </p>
 *
 * @author Admin
 * @since 2023-03-14
 */
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
@Api(tags = "医院设置信息")
public class HospitalSetController {
    @Autowired
    private HospitalSetService hospitalSetService;

    @GetMapping("/findAll")
    @ApiOperation(value = "查询所有医院设置信息")
    public R findAll() {
        List<HospitalSet> list = hospitalSetService.list();
        return R.ok().data("iteams",list);
    }

    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "根据id删除医院设置信息")
    public R deleteById(@ApiParam(name = "id",value = "医院设置id",required = true) @PathVariable Integer id) {
         hospitalSetService.removeById(id);
         return R.ok();
    }

    @PostMapping("/page/{pageNum}/{pageSize}")
    @ApiOperation(value = "根据条件进行分页查询")
    public R getPageInfo(@ApiParam(name = "pageNum",value = "当前页数",required = true) @PathVariable Integer pageNum,
                         @ApiParam(name = "pageSize",value = "每页记录数",required = true) @PathVariable Integer pageSize,
                         @RequestBody HospitalSetQueryVo hospitalSetQueryVo) {
        Page<HospitalSet> page = new Page<>(pageNum, pageSize);
        QueryWrapper<HospitalSet> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(hospitalSetQueryVo.getHosname())) {
            queryWrapper.like("hosname",hospitalSetQueryVo.getHosname());
        }
        if (!StringUtils.isEmpty(hospitalSetQueryVo.getHoscode())) {
            queryWrapper.eq("hoscode", hospitalSetQueryVo.getHoscode());
        }
        hospitalSetService.page(page, queryWrapper);
        return R.ok().data("total",page.getTotal()).data("rows",page.getRecords());
    }

    @PostMapping("/save")
    @ApiOperation(value = "新增医院设置信息")
    public R save(@RequestBody HospitalSet hospitalSet) {
        hospitalSet.setStatus(1);
        //签名密钥：当前时间戳+随机数+MD5加密
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis()+""+ random.nextInt(1000)));
        hospitalSetService.save(hospitalSet);
        return R.ok();
    }

    @GetMapping("queryById/{id}")
    @ApiOperation(value = "根据Id查询医院设置信息")
    public R queryById(@ApiParam(name = "id",value = "医院设置id",required = true) @PathVariable Integer id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return R.ok().data("item",hospitalSet);
    }

    @PostMapping("updateById")
    @ApiOperation(value = "根据Id修改医院设置信息")
    public R updateById(@RequestBody HospitalSet hospitalSet) {
        hospitalSetService.updateById(hospitalSet);
        return R.ok();
    }

    @DeleteMapping("deleteByIds")
    @ApiOperation(value = "批量删除医院设置信息")
    public R deleteByIds(@RequestBody List<Integer> ids) {
        hospitalSetService.removeByIds(ids);
        return R.ok();
    }

    @PutMapping("lockById/{id}/{status}")
    @ApiOperation(value = "锁定和解锁医院设置信息")
    public R lockById(@ApiParam(name = "id",value = "医院设置id",required = true) @PathVariable Integer id,
                      @ApiParam(name = "status",value = "状态",required = true) @PathVariable Integer status) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        hospitalSet.setStatus(status);
        hospitalSetService.updateById(hospitalSet);
        return R.ok();
    }


}

