package com.wang.miaosha_1.controller;

import com.wang.miaosha_1.domain.MiaoshaUser;
import com.wang.miaosha_1.service.GoodsService;
import com.wang.miaosha_1.service.MiaoshaUserService;
import com.wang.miaosha_1.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 商品
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private MiaoshaUserService miaoshaUserService;

    @Autowired
    private GoodsService goodsService;

    /**
     * 跳转商品列表页
     * @return
     */
    @RequestMapping("/to_list")
    public String list(Model model, MiaoshaUser miaoshaUser){
        model.addAttribute("user", miaoshaUser);
        //查询商品列表
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        return "goods_list";
    }

    /**
     * 跳转商品详情页
     * @return
     */
    @RequestMapping("/to_detail/{goodsId}")
    public String detail(Model model,
                         MiaoshaUser miaoshaUser,
                         @PathVariable("goodsId") long goodsId){
        model.addAttribute("user", miaoshaUser);
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);
        //秒杀开始时间
        long startAt = goods.getStartDate().getTime();
        //秒杀结束时间
        long endAt = goods.getEndDate().getTime();
        //当前时间
        long nowAt = System.currentTimeMillis();
        //状态
        int miaoshaStatus = 0;
        //到秒杀开始剩余时间
        int remainSeconds = 0;
        if(nowAt < startAt){//秒杀未开始，要倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)(startAt - nowAt) / 1000;
        }else if(nowAt > endAt){//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else{//秒杀正在进行
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        return "goods_detail";
    }
}
