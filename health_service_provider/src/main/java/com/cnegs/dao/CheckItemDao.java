package com.cnegs.dao;

import com.cnegs.pojo.CheckItem;

/**
 * @Author Cnegs   mybaits的mapper代理技术实现持久层操作，只需提供接口和mapper文件
 * @Date 2021/12/25 18:38
 */
public interface CheckItemDao {

    public void add(CheckItem checkItem);
}
