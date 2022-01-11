package com.cnegs.service;

import com.cnegs.entity.PageResult;
import com.cnegs.entity.QueryPageBean;
import com.cnegs.pojo.CheckItem;

import java.util.List;

/**
 * @Author Cnegs
 * @Date 2021/12/25 18:26
 */
public interface CheckItemService {

    /**
     * 添加
     */
    public void add(CheckItem checkItem);

    PageResult querypage(QueryPageBean queryPageBean);

    void delete(Integer checkItemId);

    void update(CheckItem checkItem);

    CheckItem findById(Integer checkItemId);

    List<CheckItem> findAll();

    List<Integer> selectCheckItemIdsByCheckgroupId(Integer checkgroupId);
}
