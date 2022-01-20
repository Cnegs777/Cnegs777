package com.cnegs.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cnegs.comstant.MessageConstant;
import com.cnegs.comstant.RedisMessageConstant;
import com.cnegs.entity.Result;
import com.cnegs.pojo.Order;
import com.cnegs.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @Author Cnegs
 * @Date 2022/1/16 23:07
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    /**
     * 预约提交
     * @param map
     * @return
     */
    @RequestMapping("/submit")
    public Result submitOrder(@RequestBody Map map){
        //从redis中获取缓存的验证码和用户输入的进行比对
        String telephone =(String) map.get("telephone");
        String redisKey = telephone+ RedisMessageConstant.SENDTYPE_ORDER;
        String validateCodeInRedis = jedisPool.getResource().get(redisKey);
        String validateCode = (String) map.get("validateCode");
        if(validateCodeInRedis!=null && validateCode!=null && validateCodeInRedis.equals(validateCode)){
            //验证码匹配成功，完成预约操作
            //设置预约类型：微信预约，电话预约等
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            Result result = null;
            try {
                result = orderService.order(map); //dubbo服务远程调用可能会出现网络波动等情况导致服务异常
            } catch (Exception e) {
                e.printStackTrace();
                return result;
            }
            if(result.isFlag()){
                //预约成功，发送短信通知用户
                System.out.println("尊敬的"+telephone+"用户，"+"请在"+(String) map.get("orderDate")+"做体检检查");
            }
            return result;
        }else {
            //验证码不匹配，返回信息
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }

    /**
     * 根据id查询预约信息，包括套餐信息和会员信息
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try{
            Map map = orderService.findById(id);
            //查询预约信息成功
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        }catch (Exception e){
            e.printStackTrace();
            //查询预约信息失败
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
