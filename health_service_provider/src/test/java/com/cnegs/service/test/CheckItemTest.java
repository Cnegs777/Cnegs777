package com.cnegs.service.test;

import com.cnegs.entity.PageResult;
import com.cnegs.entity.QueryPageBean;
import com.cnegs.pojo.CheckItem;
import com.cnegs.service.CheckItemService;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @Author Cnegs
 * @Date 2021/12/25 19:05
 */

@RunWith(SpringRunner.class)
public class CheckItemTest {

//    @Autowired
//    private CheckItemService checkItemService;
/*
    @Test
    public void addTest(){
        CheckItem checkItem = new CheckItem();
        checkItem.setId(3);
        checkItem.setCode("13");
        checkItem.setName("王五");
        checkItem.setAge("25");
        checkItem.setPrice(25.5f);
        checkItem.setRemark("检查组项5");
        checkItem.setSex("1");
        checkItem.setType("1");
        checkItem.setAttention("1515");
        checkItemService.add(checkItem);
    }*/

//    @Test
//    public void findpage() {
//        QueryPageBean queryPageBean = new QueryPageBean();
//        queryPageBean.setCurrentPage(1);
//        queryPageBean.setPageSize(10);
//        PageResult findpage = checkItemService.findpage(queryPageBean);
//        System.out.println("总记录数:"+findpage.getTotal());
//        System.out.println("数据集合:"+findpage.getRows());
//    }
}
