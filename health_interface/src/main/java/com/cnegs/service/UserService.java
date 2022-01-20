package com.cnegs.service;

import com.cnegs.pojo.User;

/**
 * @Author Cnegs
 * @Date 2022/1/19 23:30
 */
public interface UserService {

    User findByUserName(String userName);
}
