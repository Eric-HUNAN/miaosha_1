package com.wang.miaosha_1.dao;

import com.wang.miaosha_1.domain.Goods;
import com.wang.miaosha_1.domain.MiaoshaGoods;
import com.wang.miaosha_1.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 商品详情页goods数据库接口
 */
@Mapper
public interface GoodsDao {
    @Select("select g.*, mg.miaosha_price, mg.stock_count, mg.start_date, mg.end_date from miaosha_goods mg left join goods g on mg.goods_id=g.id")
    public List<GoodsVo> listGoodsVo();

    @Select("select g.*, mg.miaosha_price, mg.stock_count, mg.start_date, mg.end_date from miaosha_goods mg left join goods g on mg.goods_id=g.id where g.id=#{goodsId}")
    GoodsVo getGoodsVoByGoodsId(@Param("goodsId") long goodsId);

    @Update("update miaosha_goods set stock_count=stock_count-1 where goods_id=#{g.goodsId} and stock_count>0")
    public int reduceStock(@Param("g") MiaoshaGoods g);
}
