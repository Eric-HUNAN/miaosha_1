package com.wang.miaosha_1.redis;

/**
 * 用户前缀
 */

public class MiaoshaUserKey extends BasePrefix {

    private static final int TOKEN_EXPIRE_SECONDS = 3600*24*2;

    /**
     * 记录永不过期的key
     * @param prefix
     */
    private MiaoshaUserKey(String prefix) {
        super(TOKEN_EXPIRE_SECONDS, prefix);
    }

    public static MiaoshaUserKey getById = new MiaoshaUserKey("id");
    public static MiaoshaUserKey getByUserName = new MiaoshaUserKey("name");
    public static MiaoshaUserKey token = new MiaoshaUserKey("token");
}
