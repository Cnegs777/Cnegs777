package com.cnegs.service;

import com.cnegs.entity.PageResult;
import com.cnegs.entity.QueryPageBean;
import com.cnegs.pojo.CheckGroup;

import java.util.List;

/**
 * @Author Cnegs
 * @Date 2022/1/9 21:53
 */
public interface CheckGroupService {

    void add(CheckGroup checkGroup,Integer[] checkitemids);

    PageResult findpage(QueryPageBean queryPageBean);

    CheckGroup findById(Integer checkgroupId);

    void update(CheckGroup checkGroup, Integer[] checkitemids);

    List<CheckGroup> findAll();

}
