package com.wang.miaosha_1.rabbitmq;

import com.wang.miaosha_1.domain.MiaoshaUser;

/**
 * 秒杀消息
 */
public class MiaoshaMessage {
    private MiaoshaUser miaoshaUser;
    private long goodsId;

    public MiaoshaUser getMiaoshaUser() {
        return miaoshaUser;
    }

    public void setMiaoshaUser(MiaoshaUser miaoshaUser) {
        this.miaoshaUser = miaoshaUser;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }
}
