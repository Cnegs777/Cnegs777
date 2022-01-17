package com.cnegs.dao;

import com.cnegs.pojo.CheckItem;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author Cnegs   mybaits的mapper代理技术实现持久层操作，只需提供接口和mapper文件
 * @Date 2021/12/25 18:38
 */
public interface CheckItemDao {

    public void add(CheckItem checkItem);

    Page<CheckItem> selectByCondition(@Param("content") String queryString);

    void delete(@Param("id") Integer checkItemId);

    void update(CheckItem checkItem);

    long findCountByCheckItemId(@Param("id") Integer checkItemId);

    CheckItem findById(@Param("id") Integer checkItemId);

    List<CheckItem> findAll();

    List<Integer> selectCheckItemIdsByCheckgroupId(Integer checkgroupId);

    List<CheckItem> findCheckItemById(int id);
}
