package com.wang.miaosha_1.util;

import java.util.UUID;

/**
 * 生成uuid的工具类
 */
public class UUIDUtil {
    public static String uuid(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
