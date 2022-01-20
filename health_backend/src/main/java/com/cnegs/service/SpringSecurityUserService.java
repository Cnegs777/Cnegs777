package com.cnegs.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cnegs.pojo.Permission;
import com.cnegs.pojo.Role;
import com.cnegs.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Author Cnegs
 * @Date 2022/1/19 23:29
 */
@Component //此类需要让dubbo服务扫描到，在配置文件中配置dubbo包扫描到cnegs
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUserName(username);
        if(user==null){
            //用户不存在
            return null;
        }else {
            List<GrantedAuthority> list = new ArrayList<>();
            //认证成功存在该用户，完成授权操作,通过该用户的角色，授权相对应的权限
            Set<Role> roles = user.getRoles();
            for (Role role : roles) {
                //授予角色
                list.add(new SimpleGrantedAuthority(role.getKeyword()));
                Set<Permission> permissions = role.getPermissions();
                for (Permission permission : permissions) {
                    //授予权限
                    list.add(new SimpleGrantedAuthority(permission.getKeyword()));
                }
            }
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(username,user.getPassword(),list);
            return userDetails;
        }
    }
}
