package com.wang.miaosha_1.controller;

import com.wang.miaosha_1.domain.MiaoshaUser;
import com.wang.miaosha_1.domain.OrderInfo;
import com.wang.miaosha_1.result.CodeMsg;
import com.wang.miaosha_1.result.Result;
import com.wang.miaosha_1.service.GoodsService;
import com.wang.miaosha_1.service.OrderService;
import com.wang.miaosha_1.vo.GoodsVo;
import com.wang.miaosha_1.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 订单详情
 */

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVo> detail(MiaoshaUser miaoshaUser,
                            @RequestParam("orderId") String orderId){
        if(miaoshaUser == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //根据订单id获取订单信息
        long id = Long.parseLong(orderId);
        OrderInfo orderInfo = orderService.getOrderById(id);
        if(orderInfo == null){
            return Result.error(CodeMsg.ORDER_EMPTY);
        }
        //根据商品id获取商品信息
        long goodsId = orderInfo.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        //将goods、orderInfo注入OrderDetailVo
        OrderDetailVo vo = new OrderDetailVo();
        vo.setOrderInfo(orderInfo);
        vo.setGoodsVo(goods);
        return Result.success(vo);
    }
}
