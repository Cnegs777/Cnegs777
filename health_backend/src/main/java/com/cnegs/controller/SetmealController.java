package com.cnegs.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cnegs.comstant.MessageConstant;
import com.cnegs.comstant.RedisConstant;
import com.cnegs.entity.PageResult;
import com.cnegs.entity.QueryPageBean;
import com.cnegs.entity.Result;
<<<<<<< Updated upstream
import com.cnegs.pojo.CheckGroup;
=======
<<<<<<< Updated upstream
import com.cnegs.pojo.CheckGroup;
=======
>>>>>>> Stashed changes
>>>>>>> Stashed changes
import com.cnegs.pojo.Setmeal;
import com.cnegs.service.SetMealService;
import com.cnegs.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

/**
 * @Author Cnegs
 * @Date 2022/1/11 0:01
 */
@RequestMapping("/setmeal")
@RestController
public class SetmealController {

    @Reference
    private SetMealService setMealService;

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile){
        //图片原始名称（带有后缀）
        String originalFilename = imgFile.getOriginalFilename();
        String s = UUID.randomUUID().toString();
        int index = originalFilename.lastIndexOf(".");
        String fileName = s+originalFilename.substring(index - 1);
        //上传图片文件
        try {
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);
            //把图片名称缓存到redis
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }
        return new Result(true,MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
    }

    @RequestMapping("/findpage")
    public PageResult findpage(@RequestBody QueryPageBean queryPageBean){
        try {
            return setMealService.findpage(queryPageBean);
        } catch (Exception e) {
            System.out.println("套餐分页查询失败----："+e.getMessage());
            return null;
        }
    }

    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        try {
            setMealService.add(setmeal,checkgroupIds);
        } catch (Exception e) {
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }
}
