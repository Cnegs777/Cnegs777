package com.cnegs.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.cnegs.dao.CheckItemDao;
import com.cnegs.entity.PageResult;
import com.cnegs.entity.QueryPageBean;
import com.cnegs.pojo.CheckItem;
import com.cnegs.service.CheckItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author Cnegs 检查项服务
 * @Date 2021/12/25 18:31
 */
@Service(interfaceClass = CheckItemService.class) //如果加了事务注解，则要指定具体实现了哪个接口（interfaceClass）
@Transactional
public class CheckItemServiceImpl implements CheckItemService{

    @Autowired
    private CheckItemDao checkItemDao;

    /**
     * 添加
     * @param checkItem
     */
    @Override
    public void add(CheckItem checkItem) {
        System.out.println("增加检查项服务....");
        checkItemDao.add(checkItem);
    }

    @Override
    public PageResult querypage(QueryPageBean queryPageBean) {
        //使用分页插件
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<CheckItem> page = checkItemDao.selectByCondition(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void delete(Integer checkItemId) {
        //查询检查组中是否关联该检查项
        long count = checkItemDao.findCountByCheckItemId(checkItemId);
        if(count>0){
            //当前检查项被引用，不能删除
            throw new RuntimeException("当前检查项被引用，不能删除");
        }
        checkItemDao.delete(checkItemId);
    }

    @Override
    public void update(CheckItem checkItem) {
        checkItemDao.update(checkItem);
    }

    @Override
    public CheckItem findById(Integer checkItemId) {
        return checkItemDao.findById(checkItemId);
    }

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }

    /**
     * 根据检查组id查询所有关联的检查项，  t_checkgroup_checkitem表
     * @param checkgroupId
     * @return
     */
    @Override
    public List<Integer> selectCheckItemIdsByCheckgroupId(Integer checkgroupId) {
        return checkItemDao.selectCheckItemIdsByCheckgroupId(checkgroupId);
    }
}
