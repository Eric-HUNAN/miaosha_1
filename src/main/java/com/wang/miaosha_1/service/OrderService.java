package com.wang.miaosha_1.service;

import com.wang.miaosha_1.dao.OrderDao;
import com.wang.miaosha_1.domain.MiaoshaOrder;
import com.wang.miaosha_1.domain.MiaoshaUser;
import com.wang.miaosha_1.domain.OrderInfo;
import com.wang.miaosha_1.redis.OrderKey;
import com.wang.miaosha_1.redis.RedisService;
import com.wang.miaosha_1.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 秒杀订单
 */
@Service
public class OrderService {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private RedisService redisService;

    /**
     * 根据用户id、商品id查询是否已经生产秒杀订单信息
     * @param miaoshaUserId
     * @param goodsId
     * @return
     */
    public MiaoshaOrder getByUserIdAndGoodsId(long miaoshaUserId, long goodsId) {
        return orderDao.getByUserIdAndGoodsId(miaoshaUserId, goodsId);
    }

    /**
     * 从缓存中查MiaoshaOrder
     * @param userId
     * @param goodsId
     * @return
     */
    public MiaoshaOrder getOrderByUserIdAndGoodsId(long userId, long goodsId) {
        return redisService.get(OrderKey.getMiaoshaOrderByUidGid,
                    "" + userId + "_" + goodsId,
                        MiaoshaOrder.class);

    }

    /**
     * 根据订单id查询订单信息
     * @param id
     * @return
     */
    public OrderInfo getOrderById(long id) {
        return orderDao.getOrderById(id);
    }

    /**
     * 创建订单
     * @param miaoshaUser
     * @param goods
     * @return
     */
    @Transactional
    public OrderInfo createOrder(MiaoshaUser miaoshaUser, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        //未支付
        orderInfo.setStatus(0);
        orderInfo.setUserId(miaoshaUser.getId());
        //插入订单
        orderDao.insert(orderInfo);

        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(orderInfo.getId());
        miaoshaOrder.setUserId(miaoshaUser.getId());
        miaoshaOrder.setGoodsId(goods.getId());
        //生成秒杀订单
        orderDao.insertMiaoOrder(miaoshaOrder);
        //写入缓存
        redisService.set(OrderKey.getMiaoshaOrderByUidGid,
                        "" + miaoshaUser.getId() + "_" + goods.getId(),
                            miaoshaOrder);
        return orderInfo;
    }
}
