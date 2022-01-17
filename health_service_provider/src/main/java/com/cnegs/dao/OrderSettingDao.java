package com.cnegs.dao;

import com.cnegs.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author Cnegs
 * @Date 2022/1/12 23:15
 */
public interface OrderSettingDao {

    void add(OrderSetting orderSetting);
    void update(OrderSetting orderSetting);
    Long queryCountbyDate(Date date);

    //更新已预约人数
    public void editReservationsByOrderDate(OrderSetting orderSetting);

    List<OrderSetting> queryOrderSettingByMonth(Map<String, String> map);

    OrderSetting getOrderSettingByDate(@Param("orderDate") Date date);
}
