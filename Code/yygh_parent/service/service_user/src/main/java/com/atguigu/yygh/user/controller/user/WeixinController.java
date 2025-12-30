package com.atguigu.yygh.user.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.common.utils.JwtHelper;
import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.user.prop.WeixinProperties;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.user.utils.HttpClientUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Henry Guan
 * @description 微信控制层
 * @since 2023-04-20
 */
@Controller
@RequestMapping("/user/userinfo/wx")
public class WeixinController {

    @Autowired
    private WeixinProperties weixinProperties;

    @Autowired
    private UserInfoService userInfoService;
    @RequestMapping("/param")
    @ResponseBody
    public R getWeixinLoginParam() throws UnsupportedEncodingException {
        Map<String,Object> map = new HashMap<>();
        map.put("appid",weixinProperties.getAppid());
        map.put("scope","snsapi_login");
        String redirecturl = URLEncoder.encode(weixinProperties.getRedirecturl(), "UTF8");
        map.put("redirecturl",redirecturl);
        map.put("state",System.currentTimeMillis());
        return R.ok().data(map);
    }

    @GetMapping("/callback")
    public String callback(String code,String state) throws Exception {
        StringBuilder authSb = new StringBuilder();
        authSb.append("https://api.weixin.qq.com/sns/oauth2/access_token")
            .append("?appid=%s")
            .append("&secret=%s")
            .append("&code=%s")
            .append("&grant_type=authorization_code");
        String authUrl = String.format(authSb.toString(), weixinProperties.getAppid(), weixinProperties.getAppsecret(), code);
        String authResult = HttpClientUtils.get(authUrl);
        JSONObject authJson = JSONObject.parseObject(authResult);
        String accessToken = authJson.getString("access_token");
        String openid = authJson.getString("openid");

        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid",openid);
        UserInfo userInfo = userInfoService.getOne(queryWrapper);

        if (userInfo == null) {
            //首次使用微信登录，把用户的微信信息注册到userinfo表
            //给微信发送请求，获取微信的用户信息
            StringBuilder userSb = new StringBuilder();
            userSb.append("https://api.weixin.qq.com/sns/userinfo")
                    .append("?access_token=%s")
                    .append("&openid=%s");
            String userUrl = String.format(userSb.toString(), accessToken, openid);
            String userResult = HttpClientUtils.get(userUrl);
            JSONObject userJson = JSONObject.parseObject(userResult);
            String nickname = userJson.getString("nickname");

            userInfo = new UserInfo();
            userInfo.setOpenid(openid);
            userInfo.setNickName(nickname);
            userInfo.setStatus(1);
            userInfoService.save(userInfo);
        }

        if (userInfo.getStatus() == 0) {
            throw new YyghException(20001,"用户已锁定");
        }

        Map<String,Object> map = new HashMap<>();
        if (StringUtils.isEmpty(userInfo.getPhone())) {
            map.put("openid",openid);
        } else {
            map.put("openid","");
        }

        String name = StringUtils.isEmpty( userInfo.getName()) ?
                StringUtils.isEmpty(userInfo.getNickName()) ? userInfo.getPhone() : userInfo.getNickName()
                : userInfo.getName();
        map.put("name",name);
        //jwt生成token字符串
        String token = JwtHelper.createToken(userInfo.getId(), name);
        map.put("token",token);
        //跳转到前端页面
        return "redirect:http://localhost:3000/weixin/callback?token=" + map.get("token")
                + "&openid="+map.get("openid")+"&name="+URLEncoder.encode(name,"utf-8");
    }
}
