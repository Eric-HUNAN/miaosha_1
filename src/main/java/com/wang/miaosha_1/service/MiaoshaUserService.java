package com.wang.miaosha_1.service;

import com.sun.org.apache.bcel.internal.classfile.Code;
import com.wang.miaosha_1.dao.MiaoshaUserDao;
import com.wang.miaosha_1.domain.MiaoshaUser;
import com.wang.miaosha_1.exception.GlobalException;
import com.wang.miaosha_1.redis.MiaoshaUserKey;
import com.wang.miaosha_1.redis.RedisService;
import com.wang.miaosha_1.result.CodeMsg;
import com.wang.miaosha_1.util.MD5Util;
import com.wang.miaosha_1.util.UUIDUtil;
import com.wang.miaosha_1.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;


@Service
public class MiaoshaUserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    private MiaoshaUserDao miaoshaUserDao;

    @Autowired
    private RedisService redisService;

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    public MiaoshaUser getById(long id){
        //从缓存取
        MiaoshaUser miaoshaUser = redisService.get(MiaoshaUserKey.getById, "" + id,
                                                   MiaoshaUser.class);
        if(miaoshaUser != null){
            return miaoshaUser;
        }
        //从数据库取
        miaoshaUser = miaoshaUserDao.getById(id);
        redisService.set(MiaoshaUserKey.getById, "" + id, miaoshaUser);
        return miaoshaUser;
    }

    /**
     * 根据token获取MiaoshaUser
     * @param response
     * @param token
     * @return
     */
    public MiaoshaUser getByToken(HttpServletResponse response, String token) {
        if(token == null){
            return null;
        }
        MiaoshaUser miaoshaUser = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        //延长有效期
        if(miaoshaUser != null){
            addCookie(miaoshaUser, token, response);
        }
        return miaoshaUser;
    }

    /**
     * 登录
     * @param loginVo
     * @return
     */
    public boolean login(LoginVo loginVo, HttpServletResponse response){
        if(loginVo == null){
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        //判断手机号是否存在
        MiaoshaUser miaoshaUser = getById(Long.parseLong(mobile));
        if(miaoshaUser == null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIT);
        }
        //验证密码
        String dBPass = miaoshaUser.getPassword();
        String dbSalt = miaoshaUser.getSalt();
        String calcPass = MD5Util.formPassToDBPass(password, dbSalt);
        if(!calcPass.equals(dBPass)){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        //生成cookie,登录一次生成一次token
        String token = UUIDUtil.uuid();
        addCookie(miaoshaUser, token, response);
        return true;
    }

    /**
     * 生成Cookie
     * @param miaoshaUser
     * @param response
     */
    private void addCookie(MiaoshaUser miaoshaUser, String token, HttpServletResponse response){
        //键是token，值是用户信息
        redisService.set(MiaoshaUserKey.token, token, miaoshaUser);
        //生成Cookie
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        //将Cookie写到HttpServletResponse
        response.addCookie(cookie);
    }

    /**
     * 修改用户密码
     * @param id
     * @param formPass
     * @return
     */
    public boolean updatePassword(String token, long id, String formPass){
        //获取用户
        MiaoshaUser miaoshaUser = getById(id);
        if(miaoshaUser == null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIT);
        }
        //更新数据库
        MiaoshaUser user = new MiaoshaUser();
        user.setId(id);
        user.setPassword(MD5Util.formPassToDBPass(formPass, miaoshaUser.getSalt()));
        miaoshaUserDao.update(user);
        //修改缓存
        redisService.delete(MiaoshaUserKey.getById, "" + id);
        miaoshaUser.setPassword(user.getPassword());
        redisService.set(MiaoshaUserKey.token, "token", miaoshaUser);
        return true;
    }
}
