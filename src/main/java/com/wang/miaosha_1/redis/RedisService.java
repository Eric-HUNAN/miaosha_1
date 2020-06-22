package com.wang.miaosha_1.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 提供reidis服务
 */

@Service
public class RedisService {
    @Autowired
    private JedisPool jedisPool;

    /**
     * 根据key查询value
     * @param prefix
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz){
        Jedis jedis = null;
        try{
            //获取jedis
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey = prefix.getKeyPrefix() + key;
            //通过key获取value
            String value = jedis.get(realKey);
            T t = stringToBean(value, clazz);
            return t;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 设置key-value
     * @param prefix
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean set(KeyPrefix prefix, String key, T value){
        Jedis jedis = null;
        try {
            //获取jedis
            jedis = jedisPool.getResource();
            String str = beanToString(value);
            if(str==null || str.length()<=0){
                return false;
            }
            //生成真正的key
            String realKey = prefix.getKeyPrefix() + key;
            //获取有效时间
            int expireTime = prefix.expireSeconds();
            if(expireTime <= 0){
                jedis.set(realKey, str);
            }else{
                jedis.setex(realKey, expireTime, str);
            }
            return true;
        }finally{
            returnToPool(jedis);
        }
    }

    /**
     * 判断key是否存在
     * @param prefix
     * @param key
     * @return
     */
    public  boolean existKey(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try{
            //获取jedis
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey = prefix.getKeyPrefix() + key;
            return jedis.exists(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * value值加一
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long incr(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try {
            //获取jedis
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey = prefix.getKeyPrefix() + key;
            return jedis.incr(realKey);
        }finally{
            returnToPool(jedis);
        }
    }

    /**
     * value值减1
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long decr(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try {
            //获取redis
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey = prefix.getKeyPrefix() + key;
            return jedis.decr(realKey);
        }finally{
            returnToPool(jedis);
        }
    }

    /**
     * 根据key删除value
     * @param prefix
     * @param key
     * @return
     */
    public boolean delete(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try {
            //获取redis
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey = prefix.getKeyPrefix() + key;
            long res = jedis.del(realKey);
            return res > 0;
        }finally{
            returnToPool(jedis);
        }
    }

    /**
     * T对象转换为字符串
     * @param value
     * @param <T>
     * @return
     */
    public static <T> String beanToString(T value) {
        if(value == null){
            return null;
        }
        Class<?> clazz = value.getClass();
        if(clazz==int.class || clazz==Integer.class){
            return "" + value;
        }else if(clazz == String.class){
            return (String)value;
        }else if(clazz==long.class || clazz==Long.class){
            return "" + value;
        }else {
            return JSON.toJSONString(value);
        }
    }

    /**
     * 字符串转换为T对象
     * @param value
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T stringToBean(String value, Class<T> clazz) {
        if(value==null || value.length()<=0 || clazz==null){
            return null;
        }
        if(clazz==int.class || clazz==Integer.class){
            return (T)Integer.valueOf(value);
        }else if(clazz == String.class){
            return (T)value;
        }else if(clazz==long.class || clazz==Long.class){
            return (T)Long.valueOf(value);
        }else {
            return JSON.toJavaObject(JSON.parseObject(value), clazz);
        }
    }

    /**
     * 关闭连接池
     * @param jedis
     */
    private void returnToPool(Jedis jedis) {
        if(jedis != null){
            jedis.close();
        }
    }
}
