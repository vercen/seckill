package com.edu.seckill.utils;

import java.util.UUID;

/**
 * @author vercen
 * @version 1.0
 * @date 2023/4/22 14:22
 */
public class UUIDUtil {
    public static String uuid() {
        //把UUID 中的- 替换掉
        return UUID.randomUUID().toString().replace("-", "");
    }
}
