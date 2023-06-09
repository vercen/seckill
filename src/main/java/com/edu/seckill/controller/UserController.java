package com.edu.seckill.controller;

import com.edu.seckill.pojo.User;
import com.edu.seckill.service.UserService;
import com.edu.seckill.vo.RespBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author vercen
 * @version 1.0
 * @date 2023/4/23 11:32
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    //返回用户信息, 同时我们也演示如何携带参数
    @RequestMapping("/info")
    @ResponseBody
    public RespBean info(User user, String address) {
        return RespBean.success(user);
    }


    @RequestMapping("/updpwd")
    @ResponseBody
    public RespBean updatePassword(String userTicket, String password, HttpServletRequest request, HttpServletResponse response){
        return userService.updatePassword(userTicket, password, request, response);
    }
}
