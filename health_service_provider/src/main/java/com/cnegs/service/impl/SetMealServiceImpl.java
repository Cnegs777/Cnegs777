package com.cnegs.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.cnegs.comstant.RedisConstant;
import com.cnegs.dao.SetMealDao;
import com.cnegs.entity.PageResult;
import com.cnegs.entity.QueryPageBean;
import com.cnegs.pojo.Setmeal;
import com.cnegs.service.SetMealService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;


/**
 * @Author Cnegs
 * @Date 2022/1/11 0:10
 */
@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetMealDao setMealdao;

    @Autowired
    private JedisPool jedisPool;

    @Override
    public PageResult findpage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<Setmeal> page = setMealdao.findpage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //新增套餐
        try {
            String img = setmeal.getImg();
            //七牛云访问地址
            String url = "http://r5i0c7ddh.hb-bkt.clouddn.com/"+img;
            setmeal.setImg(url);
            //保存图片到redis
            setMealdao.add(setmeal);
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,img);
            Integer setmealId = setmeal.getId();
            //新增套餐与检查组之间关系
            for (Integer checkgroupId : checkgroupIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("setmeal_id",setmealId);
                map.put("checkgroup_id",checkgroupId);
                setMealdao.addSetmealCheckgroup(map);
            }
        } catch (Exception e) {
            System.out.println("新增套餐或建立关系失败----："+e.getMessage());
            e.printStackTrace();
        }
    }
}
