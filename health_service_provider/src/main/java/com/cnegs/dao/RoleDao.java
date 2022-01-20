package com.cnegs.dao;

import com.cnegs.pojo.Role;

import java.util.Set;

/**
 * @Author Cnegs
 * @Date 2022/1/20 0:13
 */
public interface RoleDao {

    Set<Role> findByUserId(Integer userId);
}
