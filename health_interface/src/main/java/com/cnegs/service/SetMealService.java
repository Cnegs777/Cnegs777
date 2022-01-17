package com.cnegs.service;

import com.cnegs.entity.PageResult;
import com.cnegs.entity.QueryPageBean;
import com.cnegs.pojo.Setmeal;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * @Author Cnegs
 * @Date 2022/1/11 0:08
 */
public interface SetMealService {

    PageResult findpage(QueryPageBean queryPageBean);

    void add(Setmeal setmeal, Integer[] checkgroupIds);

    public List<Setmeal> findAll();

    Setmeal findById(int id);

}
