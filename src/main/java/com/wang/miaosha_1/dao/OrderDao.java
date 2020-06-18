package com.wang.miaosha_1.dao;

import com.wang.miaosha_1.domain.MiaoshaOrder;
import com.wang.miaosha_1.domain.OrderInfo;
import org.apache.ibatis.annotations.*;

/**
 * 订单表数据库交互
 */
@Mapper
public interface OrderDao {
    /**
     * 根据用户id、商品id查询订单
     *
     * @param miaoshaUserId
     * @param goodsId
     * @return
     */
    @Select("select * from miaosha_order where user_id=#{miaoshaUserId} and goods_id=#{goodsId}")
    public MiaoshaOrder getByUserIdAndGoodsId(@Param("miaoshaUserId") long miaoshaUserId,
                                              @Param("goodsId") long goodsId);

    /**
     * 生成订单
     * @param orderInfo
     * @return
     */
    @Insert("insert into order_info(user_id,goods_id,delivery_addr_id,goods_name,goods_count,goods_price,order_channel,status,create_date) "+
            "values(#{userId},#{goodsId},#{deliveryAddrId},#{goodsName},#{goodsCount},#{goodsPrice},#{orderChannel},#{status},#{createDate})")
    @SelectKey(keyColumn = "id", keyProperty = "id", resultType = long.class, before = false, statement = "select last_insert_id()")
    public long insert(OrderInfo orderInfo);


    /**
     * 生成秒杀订单
     * @param miaoshaOrder
     */
    @Insert("insert into miaosha_order(user_id,order_id,goods_id) values(#{userId},#{orderId},#{goodsId})")
    public int insertMiaoOrder(MiaoshaOrder miaoshaOrder);
}