package com.edu.seckill.utils;

import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author vercen
 * @version 1.0
 * @date 2023/4/21 22:37
 * 校验手机号等
 * 正则表达式校验
 */
public class ValidatorUtil {

    private static final Pattern MOBILE_PATTERN = Pattern.compile("[1]([3-9])[0-9]{9}$");

    public static boolean isMobile(String mobile){
        if(!StringUtils.hasText(mobile)){
            return false;
        }
        Matcher matcher = MOBILE_PATTERN.matcher(mobile);
        return matcher.matches();//返回校验结果true 为正确
    }

}
