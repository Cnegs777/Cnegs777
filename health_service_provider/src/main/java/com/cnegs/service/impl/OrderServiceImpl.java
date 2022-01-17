package com.cnegs.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.cnegs.comstant.MessageConstant;
import com.cnegs.dao.MemberDao;
import com.cnegs.dao.OrderDao;
import com.cnegs.entity.Result;
import com.cnegs.pojo.Member;
import com.cnegs.pojo.Order;
import com.cnegs.pojo.OrderSetting;
import com.cnegs.service.OrderService;
import com.cnegs.service.OrderSettingService;
import com.cnegs.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author Cnegs
 * @Date 2022/1/16 23:54
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingService orderSettingService;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    @Override
    public Result order(Map map) throws Exception {
        //判断用户所选预约日期在系统内是否提前设置
        String orderDate = (String) map.get("orderDate");
        Date date = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting = orderSettingService.getOrderSettingByDate(date);
        if(orderSetting!=null){
            //查看用户所选日期的预约人数是否已满，满了就不能预约
            int number = orderSetting.getNumber(); //可预约人数
            int reservations = orderSetting.getReservations(); //已预约人数
            if(reservations>=number){
                //预约人数已满，不能预约了
                return new Result(false,MessageConstant.ORDER_FULL);
            }
            //判断用户是否在同一天重复预约同一套餐
            //先判断是否有会员
            String telephone =(String) map.get("telephone");
            Member member = null;
            Integer setmealId = null;//套餐id
            member = memberDao.findByTelephone(telephone);
            if(member!=null){
                //该用户已是会员，通过会员信息，查看订单预约是否重复
                Integer memberId = member.getId();
                String id =(String) map.get("setmealId");
                setmealId = Integer.parseInt(id);
                Order order = new Order(memberId,date,setmealId);
                //根据会员id、预约日期、套餐id查询信息
                List<Order> orderList = orderDao.findByCondition(order);
                if(orderList.size()>0){
                    //已经预约，不可重复预约
                    return new Result(false,MessageConstant.HAS_ORDERED);
                }
            }else {
                //该用户尚未开通会员，自动为该用户开通会员吗，这里仅设置必要信息，后续再补充个人信息
                member =new Member();
                String idCard =(String) map.get("idCard");
                String name =(String) map.get("name");
                String sex = (String) map.get("sex");
                member.setIdCard(idCard);
                member.setPhoneNumber(telephone);
                member.setRegTime(new Date());
                member.setName(name);
                member.setSex(sex);
                memberDao.add(member);
            }
            //预约成功，保存订单信息以及更新预约人数
            Order order = new Order();
            order.setMemberId(member.getId());
            order.setOrderDate(date);
            String id = (String) map.get("setmealId");
            setmealId = Integer.valueOf(id);
            order.setSetmealId(setmealId);
            String orderType =(String) map.get("orderType");
            order.setOrderType(orderType);
            orderDao.add(order);
            orderSetting.setReservations(orderSetting.getReservations()+1);
            orderSettingService.editReservationsByOrderDate(orderSetting);
            return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
        }else {
            System.out.println("所选日期不能进行预约");
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
    }

    //根据id查询预约信息，包括体检人信息、套餐信息
    public Map findById(Integer id) throws Exception {
        Map map = orderDao.findById4Detail(id);
        if(map != null){
            //处理日期格式
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate",DateUtils.parseDate2String(orderDate));
        }
        return map;
    }
}
