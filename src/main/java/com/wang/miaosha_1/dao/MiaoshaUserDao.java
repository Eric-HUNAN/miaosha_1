package com.wang.miaosha_1.dao;

import com.wang.miaosha_1.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * MiaoshaUser数据库接口
 */
@Mapper
public interface MiaoshaUserDao {
    /**
     * 根据id获取MiaoshaUser
     * @param id
     * @return
     */
    @Select("select * from miaosha_user where id=#{id}")
    public MiaoshaUser getById(@Param("id") long id);
}
