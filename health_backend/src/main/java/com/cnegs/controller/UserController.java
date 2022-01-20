package com.cnegs.controller;

import com.cnegs.entity.Result;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Cnegs
 * @Date 2022/1/20 1:50
 */
@RequestMapping("/user")
@RestController
public class UserController {

    @RequestMapping("/getUsername")
    public Result getUsername(){
        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user!=null){
            String username = user.getUsername();
            return new Result(true,"",username);
        }
        return null;
    }
}
