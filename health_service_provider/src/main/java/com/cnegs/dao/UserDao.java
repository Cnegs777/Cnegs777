package com.cnegs.dao;

import com.cnegs.pojo.User;

/**
 * @Author Cnegs
 * @Date 2022/1/20 0:05
 */
public interface UserDao {

    //根据username查询用户信息，认证
    User findByUserName(String username);
}
