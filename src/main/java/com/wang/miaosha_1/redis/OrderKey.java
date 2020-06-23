package com.wang.miaosha_1.redis;

/**
 * 订单模块前缀
 */
public class OrderKey extends BasePrefix{
    public OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    //通过用户id和商品id获取秒杀订单
    public static OrderKey getMiaoshaOrderByUidGid = new OrderKey(0, "moug");
}
