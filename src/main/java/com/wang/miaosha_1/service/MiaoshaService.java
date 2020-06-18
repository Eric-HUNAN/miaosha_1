package com.wang.miaosha_1.service;

import com.wang.miaosha_1.dao.GoodsDao;
import com.wang.miaosha_1.domain.Goods;
import com.wang.miaosha_1.domain.MiaoshaUser;
import com.wang.miaosha_1.domain.OrderInfo;
import com.wang.miaosha_1.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 实现秒杀功能
 */
@Service
public class MiaoshaService {
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;


    /**
     * 秒杀功能：减库存、生成订单页、生成秒杀订单
     * @param miaoshaUser
     * @param goods
     * @return
     */
    @Transactional
    public OrderInfo miaosha(MiaoshaUser miaoshaUser, GoodsVo goods) {
        //减库存
        goodsService.reduceStock(goods);
        return orderService.createOrder(miaoshaUser, goods);
    }
}
