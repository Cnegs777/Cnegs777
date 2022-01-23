package com.cnegs.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.cnegs.comstant.RedisConstant;
import com.cnegs.dao.OrderDao;
import com.cnegs.dao.SetMealDao;
import com.cnegs.entity.PageResult;
import com.cnegs.entity.QueryPageBean;
import com.cnegs.pojo.CheckGroup;
import com.cnegs.pojo.CheckItem;
import com.cnegs.pojo.Setmeal;
import com.cnegs.service.SetMealService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.*;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;


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

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Autowired
    private OrderDao orderDao;

    @Value("${out_put_path}")
    private String outPuthPath;

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
//            String url = "http://r5i0c7ddh.hb-bkt.clouddn.com/"+img;
//            setmeal.setImg(url);
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

            //在新增套餐后，要生成freemarker静态文件(方法名：合成静态页面方法：generateHtml,生成套餐列表页面,generteMobileSetMealListHtml,生成套餐详情页面：generateMobileSetmealDetailHtml)
            generteMobileSetMealStaticHtml();
        } catch (Exception e) {
            System.out.println("新增套餐或建立关系失败----："+e.getMessage());
            e.printStackTrace();
        }
    }
    private void generteMobileSetMealStaticHtml() {
        Map<String,Object> map = new HashMap<>();
        //先准备数据
        List<Setmeal> setmealList = setMealdao.findAll();
        if(setmealList!=null&&setmealList.size()>0){
            //调用生成列表页面方法
            map.put("setmealList",setmealList);
            generteMobileSetMealListHtml(map);
            generateMobileSetmealDetailHtml(setmealList);
        }else {
            System.out.println("查询套餐数据异常");
        }
    }

    private void generateMobileSetmealDetailHtml(List<Setmeal> setmealList) {
        for (Setmeal setmeal : setmealList) {
            //拿到每个套餐
            Map<String,Object> map= new HashMap<>();
            Setmeal setmeal1 = setMealdao.findById(setmeal.getId());
            map.put("setmeal",setmeal1);
            generateHtml("mobile_setmeal_detail.ftl","setmeal_detail_"+setmeal.getId()+".html",map);
        }
    }

    //【通用方法】给出数据通过模板生成静态也页面（套餐列表页面和套餐详情页面）
    private void generateHtml(String templateName,String htmlpageName,Map map) {
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        FileWriter out = null;
        try {
            Template template = configuration.getTemplate(templateName);
            out = new FileWriter(new File(outPuthPath+"/"+htmlpageName));
            //生成静态文件
            template.process(map,out);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    //生成套餐列表页面
    private void generteMobileSetMealListHtml(Map map) {
         generateHtml("mobile_setmeal.ftl","m_setmeal.html",map);
    }

    //生成套餐详情页面
    public List<Setmeal> findAll() {
        return setMealdao.findAll();
    }


    public Setmeal findById(int id) {
        return setMealdao.findById(id);
    }

    /**
     * 统计套餐预约占比情况
     * @return
     */
    @Override
    public Map<String, Object> queryOrderCount() {
        //1.先查询所有套餐获取套餐name
        List<Map<String, Object>> maps = setMealdao.queryOrderCount();
        List<String> names = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            names.add((String) map.get("name"));
        }
        Map<String,Object> map = new HashMap<>();
        map.put("setmealNames",names);
        map.put("setmealCount",maps);
        return map;
    }
}
