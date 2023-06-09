package com.edu.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.seckill.pojo.User;
import com.edu.seckill.vo.LoginVo;
import com.edu.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author vercen
 * @version 1.0
 * @date 2023/4/21 22:45
 */
public interface UserService extends IService<User> {
    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    User getUserByCookie(String userTicket,HttpServletRequest request,
                         HttpServletResponse response);

    //更改密码
    RespBean updatePassword(String userTicket, String password, HttpServletRequest request, HttpServletResponse response);
}
