package com.wang.miaosha_1.util;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验格式
 */
public class ValidateUtil {
    //校验手机号正则表达式
    private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");

    /**
     * 校验手机号是否符合格式要求
     * @param mobile
     * @return
     */
    public static boolean isMobile(String mobile){
        if(StringUtils.isEmpty(mobile)){
            return false;
        }
        Matcher m = mobile_pattern.matcher(mobile);
        return m.matches();
    }

}
