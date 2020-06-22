package com.wang.miaosha_1.rabbitmq;

import com.wang.miaosha_1.domain.MiaoshaOrder;
import com.wang.miaosha_1.domain.MiaoshaUser;
import com.wang.miaosha_1.redis.RedisService;
import com.wang.miaosha_1.result.CodeMsg;
import com.wang.miaosha_1.result.Result;
import com.wang.miaosha_1.service.GoodsService;
import com.wang.miaosha_1.service.MiaoshaService;
import com.wang.miaosha_1.service.OrderService;
import com.wang.miaosha_1.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.plugin2.message.Message;

/**
 * 消息接收者
 */
@Service
public class MQReceiver {
    private static final Logger log = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MiaoshaService miaoshaService;

    @RabbitListener(queues = MQConfig.QUEUE)
    public void receive(String msg){
        log.info("receive message:" + msg);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
    public void topicQueue1Receive(String msg){
        log.info("receive topic1 message:" + msg);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
    public void topicQueue2Receive(String msg){
        log.info("receive topic2 message:" + msg);
    }

    @RabbitListener(queues = MQConfig.HEADERS_QUEUE)
    public void receiveHeaderQueue(byte[] message) {
			log.info("header queue message:"+new String(message));
    }

    @RabbitListener(queues = MQConfig.MIAO_SHA_QUEUE)
    public void receiveMiaoshaQueue(String msg){
        log.info("receive miaosha message:" + msg);
        MiaoshaMessage mm = RedisService.stringToBean(msg, MiaoshaMessage.class);
        MiaoshaUser miaoshaUser = mm.getMiaoshaUser();
        long id = mm.getGoodsId();
        //从数据库减库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(id);
        Integer stock = goods.getStockCount();
        if(stock <= 0){
            return;
        }
        //判断是否已经秒杀过
        MiaoshaOrder order = orderService.getOrderByUserIdAndGoodsId(miaoshaUser.getId(), id);
        if(order != null){
            return;
        }
        //生成秒杀订单
        miaoshaService.miaosha(miaoshaUser, goods);
    }

}
