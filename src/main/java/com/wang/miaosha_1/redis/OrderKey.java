package com.wang.miaosha_1.redis;

/**
 * 订单模块前缀
 */
public class OrderKey extends BasePrefix{
    public OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static OrderKey getMiaoshaOrderByUidGid = new OrderKey(0, "moug");
}
