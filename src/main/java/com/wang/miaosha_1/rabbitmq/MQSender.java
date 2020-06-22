package com.wang.miaosha_1.rabbitmq;

import com.wang.miaosha_1.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.stereotype.Service;

/**
 * 消息发送者
 */
@Service
public class MQSender {
    private static final Logger log = LoggerFactory.getLogger(MQSender.class);

    @Autowired
    private AmqpTemplate amqpTemplate;

 /*   *//**
     * Direct模式 发送消息
     * @param message
     *//*
    public void send(Object message){
        String str = RedisService.beanToString(message);
        log.info("send message:" + str);
        amqpTemplate.convertAndSend(MQConfig.QUEUE, str);
    }

    *//**
     * 交换机模式
     * @param message
     *//*
    public void sendTopic(Object message){
        String str = RedisService.beanToString(message);
        log.info("send topic message:" + str);
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,
                                    "topic.key1",
                                    str + "1");
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,
                "topic.key.2",
                str + "2");
    }

    *//**
     * 广播模式
     * @param message
     *//*
    public void sendFanout(Object message){
        String str = RedisService.beanToString(message);
        log.info("send fanout message:" + str);
        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE, str);
    }

    *//**
     * header模式
     *//*
    public void sendHeader(Object message){
        String msg = RedisService.beanToString(message);
		log.info("send fanout message:"+msg);
		MessageProperties properties = new MessageProperties();
		properties.setHeader("header1", "value1");
		properties.setHeader("header2", "value2");
		Message obj = new Message(msg.getBytes(), properties);
		amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE, "", obj);
    }*/

    /**
     * 秒杀消息发送
     * @param msg
     */
    public void sendMiaoshaMessage(MiaoshaMessage msg) {
        String str = RedisService.beanToString(msg);
        log.info("send miaosha msg:" + msg);
        amqpTemplate.convertAndSend(MQConfig.MIAO_SHA_QUEUE, str);
    }
}
