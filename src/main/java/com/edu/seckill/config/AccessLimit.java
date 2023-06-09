package com.edu.seckill.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author vercen
 * @version 1.0
 * @date 2023/4/25 10:26
 * 自定义注解AccessLimit 限流
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AccessLimit {
    int second();//时间
    int maxCount();//次数
    boolean needLogin() default true;//是否需要登录
}
