package com.cnegs.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.cnegs.dao.CheckItemDao;
import com.cnegs.pojo.CheckItem;
import com.cnegs.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
}
