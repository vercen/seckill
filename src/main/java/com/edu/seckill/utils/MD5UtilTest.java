package com.edu.seckill.utils;

import org.junit.jupiter.api.Test;

/**
 * @author vercen
 * @version 1.0
 * @date 2023/4/21 21:50
 */
public class MD5UtilTest {
    @Test
    public void f1(){
        //
        String midPass = MD5Utils.inputPassToMidPass("123456");
        System.out.println(midPass);
        String dbPass = MD5Utils.midPassToDBPass(midPass, "123456");
        System.out.println(dbPass);
        String dbPass1 = MD5Utils.inputPassToDBPass("123456", "123456");
        System.out.println(dbPass1);


        String dbPass2 = MD5Utils.inputPassToDBPass("123456", "abcdef");

        System.out.println(dbPass2);

    }
}
