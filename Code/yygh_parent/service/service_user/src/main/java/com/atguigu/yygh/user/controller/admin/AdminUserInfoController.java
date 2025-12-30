package com.atguigu.yygh.user.controller.admin;

import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.vo.user.UserInfoQueryVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Henry Guan
 * @description
 * @since 2023-04-26
 */
@RestController
@RequestMapping("/administrator/userinfo")
public class AdminUserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("/{pageNum}/{pageSize}")
    public R getUserInfoPage(@PathVariable Integer pageNum, @PathVariable Integer pageSize, UserInfoQueryVo userInfoQueryVo) {
        Page<UserInfo> page = userInfoService.getUserInfoPage(pageNum,pageSize,userInfoQueryVo);
        return R.ok().data("total",page.getTotal()).data("list",page.getRecords());
    }

    @PutMapping("/lock/{id}/{status}")
    public R lock(@PathVariable Long id ,@PathVariable Integer status) {
        userInfoService.lock(id,status);
        return R.ok();
    }

    @GetMapping("/detail/{id}")
    public R detail(@PathVariable Long id) {
        Map<String,Object> map = userInfoService.detail(id);
        return R.ok().data(map);
    }

    @PutMapping("/approval/{id}/{authStatus}")
    public R approval(@PathVariable Long id,@PathVariable Integer authStatus) {
        userInfoService.approval(id,authStatus);
        return R.ok();
    }
}
