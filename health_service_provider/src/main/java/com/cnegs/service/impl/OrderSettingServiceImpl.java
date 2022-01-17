package com.cnegs.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.cnegs.dao.OrderSettingDao;
import com.cnegs.pojo.OrderSetting;
import com.cnegs.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author Cnegs
 * @Date 2022/1/12 23:01
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * excel表数据导入到Database
     *
     * @param list
     */
    @Override
    public void add(List<OrderSetting> list) {
        //当前用户有可能连续操作上传文件，如果不处理会出现重复数据
        //判断当前日期是否有上传数据，是就修改，否就新增
        if (list != null && list.size() > 0) {

            for (OrderSetting orderSetting : list) {
                Long count = orderSettingDao.queryCountbyDate(orderSetting.getOrderDate());
                if (count > 0) {
                    //今日已操作该记录
                    orderSettingDao.update(orderSetting);
                } else {
                    //没有就新增
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    /**
     * 查询当前月份的所有预约数据
     *
     * @param date
     * @return
     */
    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        //开始日期2022/1/1
        String begin = date + "-1";
        //结束日期2022/1/31
        String end = date + "-31";
        Map<String, String> map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);
        //获取这个日期内所有订单
        List<OrderSetting> list = orderSettingDao.queryOrderSettingByMonth(map);
        List<Map> mapList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (OrderSetting orderSetting : list) {
                Map<String, Object> resultMap = new HashMap<>();
                Date orderDate = orderSetting.getOrderDate();
                int date1 = orderDate.getDate();    //日期数字，几号
                //拼成前端对应所需的属性名
                resultMap.put("date",date1);
                resultMap.put("number",orderSetting.getNumber());
                resultMap.put("reservations",orderSetting.getReservations());
                mapList.add(resultMap);
            }
        }
        return mapList;
    }

    /**
     * 预约设置  可预约人数
     * @param orderSetting
     */
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        Date orderDate = orderSetting.getOrderDate();
        int number = orderSetting.getNumber();
        //根据日期查询是否已经预约设置
        long count = orderSettingDao.queryCountbyDate(orderDate);
        if(count>0){
            //已经预约过,更新
            orderSettingDao.update(orderSetting);
        }else {
            //没有预约，新增
            orderSettingDao.add(orderSetting);
        }
    }

    @Override
    public OrderSetting getOrderSettingByDate(Date date) {
        return orderSettingDao.getOrderSettingByDate(date);
    }

    /**
     * 更新体检已预约人数
     * @param orderSetting
     */
    @Override
    public void editReservationsByOrderDate(OrderSetting orderSetting) {
        orderSettingDao.editReservationsByOrderDate(orderSetting);
    }

}
