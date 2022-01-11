package com.cnegs.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cnegs.comstant.MessageConstant;
import com.cnegs.entity.PageResult;
import com.cnegs.entity.QueryPageBean;
import com.cnegs.entity.Result;
import com.cnegs.pojo.CheckItem;
import com.cnegs.service.CheckItemService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

/**
 * @Author Cnegs
 * @Date 2021/12/25 18:18
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

    @Reference
    private CheckItemService checkItemService;

    /**
     * 新增
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem){
        try {
            checkItemService.add(checkItem);
        } catch (Exception e) {
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    /**
     * 分页查询 使用mybatis插件代替手动limit分页
     * @return
     */
    @RequestMapping("/findpage")
    public PageResult findpage(@RequestBody QueryPageBean queryPageBean){
        try {
            PageResult page = checkItemService.querypage(queryPageBean);
            return page;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("检查项数据查询失败");
        }
        return null;
    }

    @RequestMapping("/findById")
    public Result findById(Integer checkItemId){

        CheckItem checkItem = checkItemService.findById(checkItemId);
        if(checkItem==null){
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
    }

    @RequestMapping("/delete")
    public Result delete(Integer checkItemId){
        try {
            checkItemService.delete(checkItemId);
        }catch (RuntimeException e){
            return new Result(false,"被检查组使用的检查项不能删除！");
        }catch (Exception e) {
            return new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    @RequestMapping("/update")
    public Result update(@RequestBody CheckItem checkItem){
        try {
            checkItemService.update(checkItem);
        } catch (Exception e) {
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    @RequestMapping("/findAll")
    public Result findAll(){

        try {
            List<CheckItem> checkItemList = checkItemService.findAll();
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItemList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    @RequestMapping("/selectCheckItemIdsByCheckgroupId")
    public Result selectCheckItemIdsByCheckgroupId(Integer checkgroupId){

        try {
            List<Integer> checkItemIds = checkItemService.selectCheckItemIdsByCheckgroupId(checkgroupId);
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }
}
