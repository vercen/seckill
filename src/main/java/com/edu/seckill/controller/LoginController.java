package com.edu.seckill.controller;

import com.edu.seckill.service.UserService;
import com.edu.seckill.vo.LoginVo;
import com.edu.seckill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author vercen
 * @version 1.0
 * @date 2023/4/21 23:24
 */
@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {
    @Resource
    UserService userService;

    /*登录页面*/
    @RequestMapping("/toLogin")
    public String toLogin(){
        return "index";
    }

    //处理用户登录请求
    @ResponseBody
    @RequestMapping("/doLogin")
    public RespBean doLogin(@Valid LoginVo loginVo, HttpServletRequest httpServletRequest , HttpServletResponse httpServletResponse){
        log.info("{}", loginVo);
        RespBean respBean = userService.doLogin(loginVo, httpServletRequest, httpServletResponse);
        return respBean;

    }
}
