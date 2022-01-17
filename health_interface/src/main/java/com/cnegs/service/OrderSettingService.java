package com.cnegs.service;

import com.cnegs.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author Cnegs
 * @Date 2022/1/12 22:59
 */
public interface OrderSettingService {

    public void add(List<OrderSetting> list);

    List<Map> getOrderSettingByMonth(String date);

    void editNumberByDate(OrderSetting orderSetting);

    OrderSetting getOrderSettingByDate(Date date);

    //更新已预约人数
    public void editReservationsByOrderDate(OrderSetting orderSetting);
}
