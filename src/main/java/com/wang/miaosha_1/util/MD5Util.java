package com.wang.miaosha_1.util;


import org.apache.commons.codec.digest.DigestUtils;

/**
 * MD5加密
 */
public class MD5Util {

    private final static String salt = "1a2b3c4d";

    /**
     * MD5操作
     * @param src
     * @return
     */
    public static String md5(String src){
        return DigestUtils.md2Hex(src);
    }

    /**
     * 输入密码转换为传输密码
     * @param inputPass
     * @return
     */
    public static String inputPassToFormPass(String inputPass){
        String str = ""+salt.charAt(0)+salt.charAt(2) + inputPass +salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    /**
     * 传输密码转换成数据库密码
     * @param formPass
     * @param salt
     * @return
     */
    public static String formPassToDBPass(String formPass, String salt){
        String str = ""+salt.charAt(0)+salt.charAt(2) + formPass +salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    /**
     * 输入密码直接转换为数据库密码
     * @param inputPass
     * @param saltDB
     * @return
     */
    public static String inputPassToDBPass(String inputPass, String saltDB){
        //输入明文密码转换为传输密码
        String formPass = inputPassToFormPass(inputPass);
        //传输密码转换为数据库密码
        String dBPass = formPassToDBPass(formPass, saltDB);
        return dBPass;
    }
}
