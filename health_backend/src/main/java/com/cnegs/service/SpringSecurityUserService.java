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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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


    public static void main(String[] args){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("admin"));//$2a$10$fiOX8fWXs6X4JZiLA7pcXeHbLbc5hsxse0mU6C4TxjTMcZ0NuP5.q
        System.out.println(encoder.encode("admin"));//$2a$10$IJAu/Gpcyzk5zparRyReH.fyrMVYsOQE4V2evat0SyQPaypZMU1r2
        /**
         *  通过打印后的信息可知,明文"123456"加密后的密文是："$2a$10$IJAu/Gpcyzk5zparRyReH.fyrMVYsOQE4V2evat0SyQPaypZMU1r2"
         *  数据库存储的密码一般都是加密的密文，即$2a$10$IJAu/Gpcyzk5zparRyReH.fyrMVYsOQE4V2evat0SyQPaypZMU1r2
         *  用户前端输入的密码一般都是密文：123456
         *  注意：通过encoder.matches()方法对比密码时候，只能用明文和密文进行对比，否则即使匹配也对比不出来
         */
        String passwordDB = "$2a$10$LPbhiutR34wKvjv3Qb8zBu7piw5hG3.IlQMAI3e/D1Y0DJ/mMSkYa";//模拟数据库加密后的密码
        String passwordDB2 = "$2a$10$fiOX8fWXs6X4JZiLA7pcXeHbLbc5hsxse0mU6C4TxjTMcZ0NuP5.q";
        if(encoder.matches("admin",passwordDB)){
            System.out.println("true1");//输出true
        }else{
            System.out.println("false1");
        }

        if(encoder.matches(passwordDB,passwordDB2)){
            System.out.println("true2");
        }else{
            System.out.println("false2");
        }
    }
}
