package com.cnegs.dao;

import com.cnegs.pojo.Permission;

import java.util.Set;

/**
 * @Author Cnegs
 * @Date 2022/1/20 0:17
 */
public interface PermissionDao {

    Set<Permission> findByRoleId(Integer roleId);
}
