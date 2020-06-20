package com.wang.miaosha_1.redis;

/**
 * 商品前缀
 */
public class GoodsKey extends BasePrefix{
    public GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    //这里设置时间主要是如果redis中页面缓存时间过长，页面的数据更新会不及时
    public static GoodsKey getGoodsList = new GoodsKey(60,"gl");

}
