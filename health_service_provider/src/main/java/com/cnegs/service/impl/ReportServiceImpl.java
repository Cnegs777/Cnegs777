package com.cnegs.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.cnegs.dao.MemberDao;
import com.cnegs.dao.OrderDao;
import com.cnegs.service.ReportService;
import com.cnegs.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Cnegs
 * @Date 2022/1/23 13:14
 */
@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    @Override
    public Map<String, Object> getBusinessReportData() throws Exception {
        //定义封装报文json格式
        Map<String,Object> datamap = new HashMap<>();
        //今日日期
        String reportDate = DateUtils.parseDate2String(new Date());
        datamap.put("reportDate",reportDate);
        //新增会员数量
        Integer memberCountByDate = memberDao.findMemberCountByDate(reportDate);
        datamap.put("todayNewMember",memberCountByDate);
        //总会员数量
        Integer memberTotalCount = memberDao.findMemberTotalCount();
        datamap.put("totalMember",memberTotalCount);
        //获取本周的会员数量 根据当前时间获取本周的第一天到当前时间的会员数量
        String firstDayOfWeek = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        Integer memberCountAfterDate = memberDao.findMemberCountAfterDate(firstDayOfWeek);
        datamap.put("thisWeekNewMember",memberCountAfterDate);
        //本月新增会员数量
        String firstDayOfMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
        Integer currentMonthCount = memberDao.findMemberCountAfterDate(firstDayOfMonth);
        datamap.put("thisMonthNewMember",currentMonthCount);

        //今日预约数
        Integer todayOrderNumber = orderDao.findOrderCountByDate(reportDate);
        datamap.put("todayOrderNumber",todayOrderNumber);
        //本周预约数
        Integer thisWeekOrderNumber = orderDao.findOrderCountAfterDate(firstDayOfWeek);
        datamap.put("thisWeekOrderNumber",thisWeekOrderNumber);


        //本月预约数
        Integer thisMonthOrderNumber = orderDao.findOrderCountAfterDate(firstDayOfMonth);
        datamap.put("thisMonthOrderNumber",thisMonthOrderNumber);

        //今日到诊数
        Integer todayVisitsNumber = orderDao.findVisitsCountByDate(reportDate);
        datamap.put("todayVisitsNumber",todayVisitsNumber);

        //本周到诊数
        Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(firstDayOfWeek);
        datamap.put("thisWeekVisitsNumber",thisWeekVisitsNumber);

        //本月到诊数
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(firstDayOfMonth);
        datamap.put("thisMonthVisitsNumber",thisMonthVisitsNumber);

        //热门套餐（取前4）
        List<Map> hotSetmeal = orderDao.findHotSetmeal();
        datamap.put("hotSetmeal",hotSetmeal);
        return datamap;
    }

}
