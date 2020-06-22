package com.wang.miaosha_1.service;

import com.wang.miaosha_1.dao.GoodsDao;
import com.wang.miaosha_1.domain.Goods;
import com.wang.miaosha_1.domain.MiaoshaOrder;
import com.wang.miaosha_1.domain.MiaoshaUser;
import com.wang.miaosha_1.domain.OrderInfo;
import com.wang.miaosha_1.redis.MiaoshaKey;
import com.wang.miaosha_1.redis.RedisService;
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

    @Autowired
    private RedisService redisService;


    /**
     * 秒杀功能：减库存、生成订单页、生成秒杀订单
     *
     * @param miaoshaUser
     * @param goods
     * @return
     */
    @Transactional
    public OrderInfo miaosha(MiaoshaUser miaoshaUser, GoodsVo goods) {
        //减库存
        boolean success = goodsService.reduceStock(goods);
        if (success) {
            return orderService.createOrder(miaoshaUser, goods);
        } else {
            setGoodsOver(goods.getId());
            return null;
        }
    }

    /**
     * 获取秒杀结果
     * @param usrId
     * @param goodsId
     * @return
     */
    public long getMiaoshaResult(long usrId, long goodsId) {
        MiaoshaOrder order = orderService.getOrderByUserIdAndGoodsId(usrId, goodsId);
        if(order != null){
            long id = order.getOrderId();
            return id;
        }else{
            boolean isOver = getGoodsOver(goodsId);
            if(isOver){
                return -1;
            }else{
                return 0;
            }
        }
    }

    /**
     * 根据商品id查询商品是否已经秒杀结束
     * @param goodsId
     * @return
     */
    private boolean getGoodsOver(long goodsId) {
        return redisService.existKey(MiaoshaKey.GOODS_OVER, "" + goodsId);
    }

    /**
     * 根据商品id设置商品秒杀的转态
     * @param goodsId
     */
    public void setGoodsOver(Long goodsId) {
        redisService.set(MiaoshaKey.GOODS_OVER, "" + goodsId, true);
    }
}
