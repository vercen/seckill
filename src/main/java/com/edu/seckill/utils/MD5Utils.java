package com.edu.seckill.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

/**
 * @author vercen
 * @version 1.0
 * @date 2023/4/21 19:16
 */
public class MD5Utils {
    public static String md5(String str){
        return DigestUtils.md5Hex(str);
    }

    private static final String SALT = "abcdefgh";

    /**加盐, 完成的是md5（pass+salt1）
     */
    public static String inputPassToMidPass(String inputPass) {
        String str = SALT.charAt(0) + inputPass + SALT.charAt(6);
        return md5(str);
    }

    /*第二次加盐*/
    public static String midPassToDBPass(String minPass,String salt){
        String str=salt.charAt(1)+minPass+salt.charAt(5);
        return md5(str);
    }

    public static String inputPassToDBPass(String inputPwd,String salt){
        String midPass = inputPassToMidPass(inputPwd);
        String dbPass = midPassToDBPass(midPass, salt);
        return dbPass;
    }
}
