package com.cnegs.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cnegs.comstant.MessageConstant;
import com.cnegs.entity.PageResult;
import com.cnegs.entity.QueryPageBean;
import com.cnegs.entity.Result;
import com.cnegs.pojo.CheckGroup;
import com.cnegs.service.CheckGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author Cnegs
 * @Date 2022/1/9 21:49
 */
@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;

    @RequestMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup,Integer[] checkitemids){
        try {
            checkGroupService.add(checkGroup,checkitemids);
        } catch (Exception e) {
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    @RequestMapping("/findpage")
    public PageResult findpage(@RequestBody QueryPageBean queryPageBean){
        try {
            PageResult pageResult = checkGroupService.findpage(queryPageBean);
            return pageResult;
        } catch (Exception e) {
            System.out.println("检查项分页查询失败");
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/findById")
    public Result findById(Integer checkgroupId){
        try {
            CheckGroup checkGroup = checkGroupService.findById(checkgroupId);
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
        } catch (Exception e) {
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    @RequestMapping("/update")
    public Result update(@RequestBody CheckGroup checkGroup,Integer[] checkitemids){
        try {
            checkGroupService.update(checkGroup,checkitemids);
        } catch (Exception e) {
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<CheckGroup> checkGroupList = checkGroupService.findAll();
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroupList);
        } catch (Exception e) {
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }
}


