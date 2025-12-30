package com.atguigu.yygh.hosp.controller.admin;

import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.model.acl.User;
import org.springframework.web.bind.annotation.*;

/**
 * @author Henry Guan
 * @description 用户登录
 * @since 2023-03-20
 */
@RestController
@RequestMapping("/admin/user")
public class UserController {

    @PostMapping("/login")
    public R login(@RequestBody User user){
        return R.ok().data("token","admin-token");
    }

    @GetMapping("/info")
    public R info(String token) {
        return R.ok().data("roles","[admin]")
                .data("introduction","I am a super administrator")
                .data("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif")
                .data("name","Super Admin");
    }

}
