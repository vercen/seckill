package com.edu.seckill.config;

import com.edu.seckill.pojo.User;
import com.edu.seckill.service.UserService;
import com.edu.seckill.utils.CookieUtil;
import com.edu.seckill.vo.RespBean;
import com.edu.seckill.vo.RespBeanEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * @author vercen
 * @version 1.0
 * @date 2023/4/25 10:50
 * 自定义拦截器
 */
@Component
public class AccessLimitInterceptor implements HandlerInterceptor {
    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate redisTemplate;

    //得到user对象，处理限流

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod){
            //先获取user对象
            User user = getUser(request, response);
            UserContext.setUser(user);
            HandlerMethod hm = (HandlerMethod) handler;
            //获取目标方法的注解AccessLimit,进行解析
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null) {
                //没有，直接放行
                return true;
            }
            int second = accessLimit.second();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();

            if (needLogin) {
                if (user == null) {
                    render(response, RespBeanEnum.SESSION_ERROR);
                    return false;
                }
            }
            String uri = request.getRequestURI();
            String key = uri + ":" + user.getId();
            ValueOperations valueOperations = redisTemplate.opsForValue();
            Integer count = (Integer) valueOperations.get(key);
//查看计数器，判断计数器
            if (count == null) {
                valueOperations.set(uri + ":" + user.getId(), 1, second, TimeUnit.SECONDS);
            } else if (count < maxCount) {
                valueOperations.increment(uri + ":" + user.getId());
            } else {
            //返回错误信息
                render(response, RespBeanEnum.ACCESS_LIMIT_REACH);
                return false;
            }
        }
        return true;

    }
    //构建返回对象
    private void render(HttpServletResponse response, RespBeanEnum sessionError)
            throws IOException {
        //System.out.println("render-" + sessionError.getMessage());
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        RespBean error = RespBean.error(sessionError);
        out.write(new ObjectMapper().writeValueAsString(error));
        out.flush();
        out.close();
    }

    //获取当前用户
    private User getUser(HttpServletRequest request, HttpServletResponse response) {
        String ticket = CookieUtil.getCookieValue(request, "userTicket");
        if (StringUtils.isEmpty(ticket)) {
            return null;
        }
        return userService.getUserByCookie(ticket, request, response);
    }
}
