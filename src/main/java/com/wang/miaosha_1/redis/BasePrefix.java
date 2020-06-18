package com.wang.miaosha_1.redis;

public abstract class BasePrefix implements KeyPrefix {
    //有效时间
    private int expireSeconds;
    //前缀
    private String prefix;

    public BasePrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    @Override
    public int expireSeconds() {//默认0代表永不过期
        return expireSeconds;
    }

    @Override
    public String getKeyPrefix() {
        String className = getClass().getSimpleName();
        return className + ":" + prefix;
    }
}
