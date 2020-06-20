package com.wang.miaosha_1.controller;

import com.wang.miaosha_1.domain.MiaoshaOrder;
import com.wang.miaosha_1.domain.MiaoshaUser;
import com.wang.miaosha_1.domain.OrderInfo;
import com.wang.miaosha_1.result.CodeMsg;
import com.wang.miaosha_1.result.Result;
import com.wang.miaosha_1.service.GoodsService;
import com.wang.miaosha_1.service.MiaoshaService;
import com.wang.miaosha_1.service.OrderService;
import com.wang.miaosha_1.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 秒杀
 */
@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MiaoshaService miaoshaService;

    @RequestMapping(value = "/do_miaosha", method = RequestMethod.POST)
    @ResponseBody
    public Result<OrderInfo> miaosha(MiaoshaUser miaoshaUser,
                                     @RequestParam("goodsId") String goodsId){
        long id = Long.parseLong(goodsId);
        if(miaoshaUser == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(id);
        Integer stock = goods.getStockCount();
        if(stock <= 0){
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //判断用户是否已经进行过秒杀  --优化（从缓存中查）
        MiaoshaOrder order = orderService.getOrderByUserIdAndGoodsId(miaoshaUser.getId(), id);
        //MiaoshaOrder order = orderService.getByUserIdAndGoodsId(miaoshaUser.getId(), id);
        if(order != null){
            return Result.error(CodeMsg.MIAO_SHA_REPEATABLE);
        }
        //写库存 下订单 写入秒杀订单
        OrderInfo orderInfo = miaoshaService.miaosha(miaoshaUser, goods);
        return Result.success(orderInfo);
    }
}
