package com.cnegs.dao;

import com.cnegs.pojo.Setmeal;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

<<<<<<< Updated upstream
=======
<<<<<<< Updated upstream
=======
import java.util.List;
>>>>>>> Stashed changes
>>>>>>> Stashed changes
import java.util.Map;

/**
 * @Author Cnegs
 * @Date 2022/1/11 0:13
 */
public interface SetMealDao {

    Page<Setmeal> findpage(@Param("queryString") String queryString);

    void add(Setmeal setmeal);

    void addSetmealCheckgroup(Map<String, Integer> map);
<<<<<<< Updated upstream
=======
<<<<<<< Updated upstream
=======

    public List<Setmeal> findAll();

    public Setmeal findById(@Param("id") int id);
>>>>>>> Stashed changes
>>>>>>> Stashed changes
}
