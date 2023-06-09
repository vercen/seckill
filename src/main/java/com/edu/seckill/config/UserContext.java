package com.edu.seckill.config;

import com.edu.seckill.pojo.User;

/**
 * @author vercen
 * @version 1.0
 * @date 2023/4/25 10:46
 * 用来存储拦截器获取的user 对象.
 */
public class UserContext {
    //每个线程都有自己的threadlocal，存在这里不容易混乱,线程安全
    private static ThreadLocal<User> userHolder = new ThreadLocal<>();
    public static void setUser(User user) {
        userHolder.set(user);
    }
    public static User getUser() {
        return userHolder.get();
    }
}
