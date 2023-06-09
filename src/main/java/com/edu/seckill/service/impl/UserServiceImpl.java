package com.edu.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.seckill.exception.GlobalException;
import com.edu.seckill.mapper.UserMapper;
import com.edu.seckill.pojo.User;
import com.edu.seckill.service.UserService;
import com.edu.seckill.utils.CookieUtil;
import com.edu.seckill.utils.MD5Utils;
import com.edu.seckill.utils.UUIDUtil;
import com.edu.seckill.utils.ValidatorUtil;
import com.edu.seckill.vo.LoginVo;
import com.edu.seckill.vo.RespBean;
import com.edu.seckill.vo.RespBeanEnum;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author vercen
 * @version 1.0
 * @date 2023/4/21 22:48
 */
@Service
public class UserServiceImpl  extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        //使用自定义注解校验器loginVo
//        //判断是否为空参数校验
//        if (!StringUtils.hasText(mobile)
//                || !StringUtils.hasText(password)) {
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
//        }
//        //手机校验
//        if (!ValidatorUtil.isMobile(mobile)) {
//            return RespBean.error(RespBeanEnum.MOBILE_ERROR);
//        }
        //查询数据库
        User user = userMapper.selectById(mobile);
        if (user == null) {
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }
        /*加密加盐对比数据库*/
        if (!MD5Utils.midPassToDBPass(password, user.getSlat()).equals(user.getPassword())) {
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }
        String uuid = UUIDUtil.uuid();
        //登录成功用户保存到session
        //request.getSession().setAttribute(uuid, user);
        //实现分布式session
        redisTemplate.opsForValue().set("user:"+uuid, user);
        CookieUtil.setCookie(request, response,"userTicket", uuid);


        return RespBean.success(uuid);
    }

    @Override
    public User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response) {
        if (!StringUtils.hasText(userTicket)) {
            return null;
        }
        User user   = (User)redisTemplate.opsForValue().get("user:" + userTicket);

        if (user!=null){
            CookieUtil.setCookie(request, response,"userTicket", userTicket);
        }

        return user;
    }

    @Override
    public RespBean updatePassword(String userTicket, String password, HttpServletRequest request, HttpServletResponse response) {
        //更新用户密码, 同时删除用户在Redis 的缓存对象
        User user = getUserByCookie(userTicket, request, response);
        if (user == null) {
            throw new GlobalException(RespBeanEnum.MOBILE_NOT_EXIST);
        }
        user.setPassword(MD5Utils.inputPassToDBPass(password, user.getSlat()));
        int i = userMapper.updateById(user);
        if (i == 1) {
        //删除redis
            redisTemplate.delete("user:" + userTicket);
            return RespBean.success();
        }
        return RespBean.error(RespBeanEnum.PASSWORD_UPDATE_FAIL);

    }
}
