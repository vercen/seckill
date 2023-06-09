package com.edu.seckill.config;

import com.edu.seckill.pojo.User;
import com.edu.seckill.service.UserService;
import com.edu.seckill.utils.CookieUtil;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author vercen
 * @version 1.0
 * @date 2023/4/22 18:42
 * 自定义解析器
 */
@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    @Resource
    private UserService userService;
    //如果这个方法返回true 才会执行下面的resolveArgument 方法
    //返回false 不执行下面的方法
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        //获取参数是不是user 类型
        Class<?> aClass = methodParameter.getParameterType();
        //如果为t, 就执行resolveArgument
        return aClass == User.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
//        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
//        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
//        String ticket = CookieUtil.getCookieValue(request, "userTicket");
//        if (!StringUtils.hasText(ticket)) {
//            return null;
//        }
//        //根据cookie-ticket 到Redis 获取User
//        return userService.getUserByCookie(ticket, request, response);
        return UserContext.getUser();
    }

}
