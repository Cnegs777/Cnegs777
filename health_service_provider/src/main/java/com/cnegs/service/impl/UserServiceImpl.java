package com.cnegs.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.cnegs.dao.PermissionDao;
import com.cnegs.dao.RoleDao;
import com.cnegs.dao.UserDao;
import com.cnegs.pojo.Permission;
import com.cnegs.pojo.Role;
import com.cnegs.pojo.User;
import com.cnegs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * @Author Cnegs
 * @Date 2022/1/20 0:04
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    @Override
    public User findByUserName(String userName) {
        User user = userDao.findByUserName(userName);
        Set<Role> roles = roleDao.findByUserId(user.getId());
        if(roles!=null&&roles.size()>0){
            for (Role role : roles) {
                //设置权限
                Set<Permission> permissions = permissionDao.findByRoleId(role.getId());
                if(permissions!=null&&permissions.size()>0){
                    role.setPermissions(permissions);
                }
            }
            //设置角色
            user.setRoles(roles);
        }
        return user;
    }
}
