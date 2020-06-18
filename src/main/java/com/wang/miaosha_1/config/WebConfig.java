package com.wang.miaosha_1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 参数中若出现MiaoshaUser对象，会自动进行赋值
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private MiaoshaUserArgumentResolver miaoshaUserArgumentResolver;

    /**
     * 给Controller中的参数HttpServletResponse、Cookie赋值
     * @param argumentResolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(miaoshaUserArgumentResolver);
    }
}
