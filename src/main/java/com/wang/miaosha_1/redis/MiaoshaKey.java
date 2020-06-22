package com.wang.miaosha_1.redis;

/**
 * 秒杀前缀
 */
public class MiaoshaKey extends BasePrefix{
    public MiaoshaKey(String prefix) {
        super(0, prefix);
    }

    public static final MiaoshaKey GOODS_OVER = new MiaoshaKey("go");
}
