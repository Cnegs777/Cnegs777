package com.cnegs.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cnegs.comstant.MessageConstant;
import com.cnegs.entity.Result;
import com.cnegs.pojo.OrderSetting;
import com.cnegs.service.OrderSettingService;
import com.cnegs.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author Cnegs
 * @Date 2022/1/12 22:42
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile){
        try {
            //获取到excel所有的工作薄以及工作表中数据
            List<String[]> list = POIUtils.readExcel(excelFile);
            //把数据封装到实体对象
            List<OrderSetting> orderSettingList = new ArrayList<>();
            for (String[] strings : list) {
                //获取到每一行数据
                String date = strings[0];
                String mostPeople = strings[1];
                //封装为实体对象
                OrderSetting orderSetting = new OrderSetting(new Date(date),Integer.parseInt(mostPeople));
                orderSettingList.add(orderSetting);
            }
            //数据封装完成后导入到数据库
            orderSettingService.add(orderSettingList);
            return new Result(true,MessageConstant.IMPORT_ORDERSETTING_SUCCESS,orderSettingList);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }


    /**
     * 获取指定日期，包括月份统一
     * @param date
     * @return
     */
    @RequestMapping("/getCurrentYearAndMonth")
    public Result getCurrentYearAndMonth(String date){
        //查询出一个月的预约数据，调用service层查询预约数据,因为前端需要的参数与返回参数名称不符，则用map封装比较合适,
        try {
            List<Map> list = orderSettingService.getOrderSettingByMonth(date);
            return new Result(false, MessageConstant.QUERY_ORDER_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }

    /**
     * 获取指定日期，包括月份统一
     * @param orderSetting
     * @return
     */
    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        try {
            orderSettingService.editNumberByDate(orderSetting);
            return new Result(false, MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
    }

}
