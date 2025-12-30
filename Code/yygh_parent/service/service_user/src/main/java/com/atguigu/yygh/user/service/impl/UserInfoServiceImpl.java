package com.atguigu.yygh.user.service.impl;


import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.utils.JwtHelper;
import com.atguigu.yygh.enums.AuthStatusEnum;
import com.atguigu.yygh.enums.StatusEnum;
import com.atguigu.yygh.model.user.Patient;
import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.user.mapper.UserInfoMapper;
import com.atguigu.yygh.user.service.PatientService;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.vo.user.LoginVo;
import com.atguigu.yygh.vo.user.UserAuthVo;
import com.atguigu.yygh.vo.user.UserInfoQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Henry Guan
 * @since 2023-04-19
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PatientService patientService;

    @Override
    public Map<String, Object> login(LoginVo loginVo) {
        UserInfo userInfo = null;

        //1.获取用户的手机号和验证码
        String phone = loginVo.getPhone();
        String code = loginVo.getCode();
        //2.对接收的手机号和验证码做非空判断
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)) {
            throw new YyghException(20001,"手机号或验证码有误");
        }
        //3.判断验证码是否正确
        String redisCode = (String) redisTemplate.opsForValue().get(phone);
        if (!code.equals(redisCode)) {
            throw new YyghException(20001,"验证码有误");
        }

        if (StringUtils.isEmpty(loginVo.getOpenid())) {
            //4.手机号是否首次登录，注册用户信息
            QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
            wrapper.eq("phone",phone);
            userInfo = baseMapper.selectOne(wrapper);
            if (userInfo == null) {
                userInfo = new UserInfo();
                userInfo.setPhone(phone);
                baseMapper.insert(userInfo);
                userInfo.setStatus(1);
            }
            //5.验证用户的status
            if (userInfo.getStatus() == 0) {
                throw new YyghException(20001,"用户已锁定");
            }
        } else {
            //首次使用微信登录，并且强制绑定手机号
            QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
            wrapper.eq("openid",loginVo.getOpenid());
            userInfo = baseMapper.selectOne(wrapper);

            QueryWrapper<UserInfo> phWrapper = new QueryWrapper<>();
            phWrapper.eq("phone",phone);
            UserInfo phUserInfo = baseMapper.selectOne(phWrapper);
            if (phUserInfo == null) {
                userInfo.setPhone(phone);
                baseMapper.updateById(userInfo);
            } else {
                phUserInfo.setOpenid(userInfo.getOpenid());
                phUserInfo.setNickName(userInfo.getNickName());
                baseMapper.updateById(phUserInfo);
                baseMapper.deleteById(userInfo.getId());
            }



        }

        //6.返回用户信息
        Map<String,Object> map = new HashMap<>();
        String name = StringUtils.isEmpty( userInfo.getName()) ?
                StringUtils.isEmpty(userInfo.getNickName()) ? userInfo.getPhone() : userInfo.getNickName()
                : userInfo.getName();
        map.put("name",name);
        //jwt生成token字符串
        String token = JwtHelper.createToken(userInfo.getId(), name);
        map.put("token",token);
        return map;
    }

    @Override
    public UserInfo getUserInfo(Long userId) {
        UserInfo userInfo = baseMapper.selectById(userId);
        userInfo.getParam().put("authStatusString", AuthStatusEnum.getStatusNameByStatus(userInfo.getAuthStatus()));
        return userInfo;
    }

    @Override
    public void saveUserAuth(String token, UserAuthVo userAuthVo) {
        Long userId = JwtHelper.getUserId(token);
        UserInfo userInfo = new UserInfo();
        userInfo.setId(userId);
        userInfo.setName(userAuthVo.getName());
        userInfo.setCertificatesType(userAuthVo.getCertificatesType());
        userInfo.setCertificatesNo(userAuthVo.getCertificatesNo());
        userInfo.setCertificatesUrl(userAuthVo.getCertificatesUrl());
        userInfo.setAuthStatus(AuthStatusEnum.AUTH_RUN.getStatus());
        baseMapper.updateById(userInfo);
    }

    @Override
    public Page<UserInfo> getUserInfoPage(Integer pageNum, Integer pageSize, UserInfoQueryVo userInfoQueryVo) {
        Page<UserInfo> page = new Page<>(pageNum,pageSize);
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();

        if (!StringUtils.isEmpty(userInfoQueryVo.getKeyword())) {
            queryWrapper.like("name",userInfoQueryVo.getKeyword()).or().eq("phone",userInfoQueryVo.getKeyword());
        }
        if (!StringUtils.isEmpty(userInfoQueryVo.getStatus())) {
            queryWrapper.eq("status",userInfoQueryVo.getStatus());
        }
        if (!StringUtils.isEmpty(userInfoQueryVo.getAuthStatus())) {
            queryWrapper.eq("auth_status",userInfoQueryVo.getAuthStatus());
        }
        if (!StringUtils.isEmpty(userInfoQueryVo.getCreateTimeBegin())) {
            queryWrapper.ge("create_time",userInfoQueryVo.getCreateTimeBegin());
        }
        if (!StringUtils.isEmpty(userInfoQueryVo.getCreateTimeEnd())) {
            queryWrapper.le("create_time",userInfoQueryVo.getCreateTimeEnd());
        }
        Page<UserInfo> selectPage = baseMapper.selectPage(page, queryWrapper);
        for (UserInfo userInfo : selectPage.getRecords()) {
            this.packageUserinfo(userInfo);
        }
        return selectPage;
    }

    @Override
    public void lock(Long id, Integer status) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        userInfo.setStatus(status);
        baseMapper.updateById(userInfo);
    }

    @Override
    public Map<String, Object> detail(Long id) {
        UserInfo userInfo = baseMapper.selectById(id);
        List<Patient> patients = patientService.getByUserId(userInfo.getId());
        HashMap<String, Object> map = new HashMap<>();
        map.put("userInfo",userInfo);
        map.put("patients",patients);
        return map;
    }

    @Override
    public void approval(Long id, Integer authStatus) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        userInfo.setAuthStatus(authStatus);
        baseMapper.updateById(userInfo);
    }

    private void packageUserinfo(UserInfo userInfo) {
        userInfo.getParam().put("statusString", StatusEnum.getNameByStatus(userInfo.getStatus()));
        userInfo.getParam().put("authStatusString",AuthStatusEnum.getStatusNameByStatus(userInfo.getAuthStatus()));
    }
}
