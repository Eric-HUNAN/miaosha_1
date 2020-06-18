package com.wang.miaosha_1.controller;

import com.wang.miaosha_1.domain.MiaoshaOrder;
import com.wang.miaosha_1.domain.MiaoshaUser;
import com.wang.miaosha_1.domain.OrderInfo;
import com.wang.miaosha_1.result.CodeMsg;
import com.wang.miaosha_1.service.GoodsService;
import com.wang.miaosha_1.service.MiaoshaService;
import com.wang.miaosha_1.service.OrderService;
import com.wang.miaosha_1.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @RequestMapping("/do_miaosha")
    public String miaosha(Model model,
                          MiaoshaUser miaoshaUser,
                          @RequestParam("goodsId") long goodsId){
        model.addAttribute("user", miaoshaUser);
        if(miaoshaUser == null){
            return "login";
        }
        //判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        Integer stock = goods.getStockCount();
        if(stock <= 0){
            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
            return "miaosha_fail";
        }
        //判断用户是否已经进行过秒杀
        MiaoshaOrder order = orderService.getByUserIdAndGoodsId(miaoshaUser.getId(), goodsId);
        if(order != null){
            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_REPEATABLE);
            return "miaosha_fail";
        }
        //写库存 下订单 写入秒杀订单
        OrderInfo orderInfo = miaoshaService.miaosha(miaoshaUser, goods);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goods);
        return "order_detail";
    }
}
