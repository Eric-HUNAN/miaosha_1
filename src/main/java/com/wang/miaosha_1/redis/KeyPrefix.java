package com.wang.miaosha_1.redis;

public interface KeyPrefix {
    /**
     * 有效时间
     * @return
     */
    public int expireSeconds();

    /**
     * 键的前缀
     * @return
     */
    public String getKeyPrefix();
}
