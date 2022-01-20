package com.cnegs.controller;

import com.cnegs.comstant.MessageConstant;
import com.cnegs.comstant.RedisMessageConstant;
import com.cnegs.entity.Result;
import com.cnegs.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * @Author Cnegs
 * 套餐预约发送验证码
 * @Date 2022/1/16 20:06
 */
@RequestMapping("/validateCode")
@RestController
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/send4Order")
    public Result getCheckCode(String telephone){
        //随机生成4位数验证码
        try {
            Integer checkCode = ValidateCodeUtils.generateValidateCode(6);
            System.out.println("套餐预约的验证码为："+checkCode);
            //把验证码缓存到redis中5分钟，5分钟后视为过期失效
            String setex = null;
            if(!StringUtils.isEmpty(telephone)){
                setex = jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER, 300, checkCode + "");
                System.out.println("体检预约验证码发送成功："+setex);
            }
            return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS,setex);
        } catch (Exception e) {
            return new Result(false,"发送验证码失败，请稍后尝试重新发送");
        }
    }

    @RequestMapping("/send4Login")
    public Result getLoginCheckCode(String telephone){
        //随机生成4位数验证码
        try {
            Integer checkCode = ValidateCodeUtils.generateValidateCode(6);
            System.out.println("手机快速登录的验证码为："+checkCode);
            //把验证码缓存到redis中5分钟，5分钟后视为过期失效
            String setex = null;
            if(!StringUtils.isEmpty(telephone)){
                setex = jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN, 300, checkCode + "");
                System.out.println("手机快速登录验证码发送成功："+setex);
            }
            return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS,setex);
        } catch (Exception e) {
            return new Result(false,"发送验证码失败，请稍后尝试重新发送");
        }
    }
}
