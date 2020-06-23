package com.wang.miaosha_1.controller;

import com.wang.miaosha_1.domain.MiaoshaOrder;
import com.wang.miaosha_1.domain.MiaoshaUser;
import com.wang.miaosha_1.domain.OrderInfo;
import com.wang.miaosha_1.rabbitmq.MQSender;
import com.wang.miaosha_1.rabbitmq.MiaoshaMessage;
import com.wang.miaosha_1.redis.GoodsKey;
import com.wang.miaosha_1.redis.RedisService;
import com.wang.miaosha_1.result.CodeMsg;
import com.wang.miaosha_1.result.Result;
import com.wang.miaosha_1.service.GoodsService;
import com.wang.miaosha_1.service.MiaoshaService;
import com.wang.miaosha_1.service.OrderService;
import com.wang.miaosha_1.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 秒杀
 */
@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MiaoshaService miaoshaService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MQSender mqSender;

    //内存标记，减少数据库的访问
    private Map<Long, Boolean> localOverMap = new HashMap<Long, Boolean>();

    /**
     * 系统初始化，将数据库中的库存信息加载到redis中
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        if(goodsList == null){
            return ;
        }
        for(GoodsVo goods : goodsList){
            redisService.set(GoodsKey.getMiaoshaGoodsStock,"" + goods.getId(), goods.getStockCount());
            localOverMap.put(goods.getId(), false);
        }
    }

    @RequestMapping(value = "/do_miaosha", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> miaosha(MiaoshaUser miaoshaUser,
                                     @RequestParam("goodsId") String goodsId){
        long id = Long.parseLong(goodsId);
        if(miaoshaUser == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        Boolean over = localOverMap.get(id);
        if(over){
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        //查看redis中秒杀商品的库存，若小于0直接返回
        long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, "" + id);
        if(stock < 0){
            localOverMap.put(id, true);
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        //判断用户是否已经进行过秒杀  --优化（从缓存中查）
        MiaoshaOrder order = orderService.getOrderByUserIdAndGoodsId(miaoshaUser.getId(), id);
        //MiaoshaOrder order = orderService.getByUserIdAndGoodsId(miaoshaUser.getId(), id);
        if(order != null){
            return Result.error(CodeMsg.MIAO_SHA_REPEATABLE);
        }

        //入队
        MiaoshaMessage msg = new MiaoshaMessage();
        msg.setMiaoshaUser(miaoshaUser);
        msg.setGoodsId(id);
        mqSender.sendMiaoshaMessage(msg);

        return Result.success(0);//0代表排队中

        /*//判断库存
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
        return Result.success(orderInfo);*/
    }

    /**
     * 获取秒杀结果
     * 返回订单id，表示成功
     * 返回-1，表示失败
     * 返回0，排队中
     * @param miaoshaUser
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> miaoshaResult(MiaoshaUser miaoshaUser,
                                   @RequestParam("goodsId") String goodsId) {
        long id = Long.parseLong(goodsId);
        if (miaoshaUser == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result = miaoshaService.getMiaoshaResult(miaoshaUser.getId(), id);
        return Result.success(result);
    }

    }
