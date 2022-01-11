package com.cnegs.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.cnegs.dao.CheckGroupDao;
import com.cnegs.entity.PageResult;
import com.cnegs.entity.QueryPageBean;
import com.cnegs.pojo.CheckGroup;
import com.cnegs.service.CheckGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Cnegs
 * @Date 2022/1/9 21:54
 */

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 新增检查项，并关联检查项与检查组的关系
     * @param checkGroup
     * @param checkitemids
     */
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemids) {
        //新增检查组
        try {
            checkGroupDao.add(checkGroup);
            //通过sql的selectkey标签获取添加后自增的id
            Integer checkGroupId = checkGroup.getId();
            //设置检查组与检查项之间关系
            for (Integer checkitemid : checkitemids) {
                Map<String,Integer> map= new HashMap<>();
                map.put("checkgroup_id",checkGroupId);
                map.put("checkitem_id",checkitemid);
                checkGroupDao.setCheckgroupAndCheckitem(map);
            }
        } catch (Exception e) {
            System.out.println("新增检查组内容或更新关系表失败----"+e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public PageResult findpage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<CheckGroup> page = checkGroupDao.findpage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public CheckGroup findById(Integer checkgroupId) {
        return checkGroupDao.findById(checkgroupId);
    }

    @Override
    public void update(CheckGroup checkGroup, Integer[] checkitemids) {
        //查看是否有该检查组--暂不实现
        try {
            //更新检查组
            checkGroupDao.update(checkGroup);
            //先删除原关系
            checkGroupDao.deleteById(checkGroup.getId());
            //更新表关系
            Integer checkGroupId = checkGroup.getId();
            //设置检查组与检查项之间关系
            for (Integer checkitemid : checkitemids) {
                Map<String,Integer> map= new HashMap<>();
                map.put("checkgroup_id",checkGroupId);
                map.put("checkitem_id",checkitemid);
                checkGroupDao.update_checkgroup_checkitem(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("检查组内容或更新关系表失败----"+e.getMessage());
        }
    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }
}
