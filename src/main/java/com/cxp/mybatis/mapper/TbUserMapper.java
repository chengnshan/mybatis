package com.cxp.mybatis.mapper;

import com.cxp.mybatis.pojo.User;

/**
 * @program: mybatis
 * @description:
 * @author: cheng
 * @create: 2019-08-30 19:50
 */
public interface TbUserMapper {

    /**
     * 根据id查询用户信息
     *
     * @param id
     * @return
     */
    public User queryUserById(String id);
}
