package com.cnegs.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.cnegs.comstant.MessageConstant;
import com.cnegs.comstant.RedisMessageConstant;
import com.cnegs.entity.Result;
import com.cnegs.pojo.Member;
import com.cnegs.service.MemberService;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Date;
import java.util.Map;

/**
 * @Author Cnegs
 * @Date 2022/1/17 22:44
 */
@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    @RequestMapping("/login")
    public Result login(HttpServletResponse response,@RequestBody Map map){
        //校验验证码
        String telephone = (String) map.get("telephone");
        //从redis中获取验证码
        String checkCodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        String validateCode = (String) map.get("validateCode");
        if(validateCode!=null&&checkCodeInRedis!=null&&validateCode.equals(checkCodeInRedis)){
            //验证码匹配成功,查看用户是否有会员，没有则自动注册
            Member member = memberService.findById(telephone);
            if(member==null){
                //完成注册
                member = new Member();
                member.setRegTime(new Date());
                member.setPhoneNumber(telephone);
                memberService.add(member);
            }
            //已有会员
            //设置cookie到客户端，存活时长30天
            Cookie cookie = new Cookie("login_member_telephone",telephone);
            cookie.setPath("/");
            cookie.setMaxAge(30*60*60*24);
            response.addCookie(cookie);
            //将会员信息写到redis并指定缓存时长为30分钟
            String memberJson = JSON.toJSONString(member);
            jedisPool.getResource().setex(telephone,60*30,memberJson);
            return new Result(true,MessageConstant.LOGIN_SUCCESS,member);
        }
        System.out.println("验证码输入有误");
        return new Result(false, MessageConstant.VALIDATECODE_ERROR);
    }
}
