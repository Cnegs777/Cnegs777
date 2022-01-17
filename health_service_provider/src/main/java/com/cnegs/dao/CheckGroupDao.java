package com.cnegs.dao;

import com.cnegs.pojo.CheckGroup;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author Cnegs
 * @Date 2022/1/9 22:03
 */
public interface CheckGroupDao {

    void add(CheckGroup checkGroup);

    void setCheckgroupAndCheckitem(Map<String, Integer> map);

    Page<CheckGroup> findpage(@Param("queryString") String queryString);

    CheckGroup findById(Integer checkgroupId);

    void update(CheckGroup checkGroup);

    void update_checkgroup_checkitem(Map<String, Integer> map);

    void deleteById(Integer id);

    List<CheckGroup> findAll();

    List<CheckGroup> findcheckgroupById(int id);

}
