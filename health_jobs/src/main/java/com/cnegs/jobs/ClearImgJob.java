package com.cnegs.jobs;

import com.cnegs.comstant.RedisConstant;
import com.cnegs.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * @Author Cnegs
 * @Date 2022/1/12 0:10
 */
public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;

    public void clearImg(){
        System.out.println("-------------定时任务开始-清理七牛云垃圾图片--------------");
        //大集合-小集合的差值，差值就是垃圾图片删除处理
        Set<String> imgSet = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if(imgSet!=null&&imgSet.size()>0){
            for (String img : imgSet) {
                QiniuUtils.deleteFileFromQiniu(img);
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,img);
            }
        }
    }
}
