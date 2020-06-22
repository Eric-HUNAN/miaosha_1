package com.wang.miaosha_1.service;

import com.wang.miaosha_1.dao.GoodsDao;
import com.wang.miaosha_1.domain.MiaoshaGoods;
import com.wang.miaosha_1.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品详情页Service接口
 */

@Service
public class GoodsService {
    @Autowired
    private GoodsDao goodsDao;

    /**
     * 查询所有商品页、秒杀商品页信息
     * @return
     */
    public List<GoodsVo> listGoodsVo(){
        return goodsDao.listGoodsVo();
    }

    /**
     * 根据goodsI的查询商品页、秒杀商品页信息
     * @param goodsId
     * @return
     */
    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    /**
     * 减库存
     * @param goods
     * @return
     */
    public boolean reduceStock(GoodsVo goods) {
        MiaoshaGoods g = new MiaoshaGoods();
        g.setGoodsId(goods.getId());
        long res = goodsDao.reduceStock(g);
        return res > 0;
    }
}
