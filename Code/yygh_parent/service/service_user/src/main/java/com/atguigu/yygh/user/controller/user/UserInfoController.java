package com.atguigu.yygh.user.controller.user;


import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.common.utils.JwtHelper;
import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.vo.user.LoginVo;
import com.atguigu.yygh.vo.user.UserAuthVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author Henry Guan
 * @since 2023-04-19
 */
@RestController
@RequestMapping("/user/userinfo")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/login")
    public R login(@RequestBody LoginVo loginVo) {
        Map<String, Object> map = userInfoService.login(loginVo);
        return R.ok().data(map);
    }

    /*@GetMapping("/info")
    public R getUserInfo(HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtHelper.getUserId(token);
        UserInfoQueryVo userInfoQueryVo = userInfoService.getUserInfo(userId);
        return R.ok().data("userInfoQueryVo",userInfoQueryVo);
    }*/

    @GetMapping("/info")
    public R getUserInfo(@RequestHeader String token) {
        Long userId = JwtHelper.getUserId(token);
        UserInfo userInfo = userInfoService.getUserInfo(userId);
        return R.ok().data("userInfo",userInfo);
    }

    @PostMapping("/saveUserAuth")
    public R saveUserAuth(@RequestHeader String token, @RequestBody UserAuthVo userAuthVo) {
        userInfoService.saveUserAuth(token,userAuthVo);
        return R.ok();
    }
}

