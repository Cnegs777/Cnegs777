package com.cnegs.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cnegs.comstant.MessageConstant;
import com.cnegs.entity.Result;
import com.cnegs.service.MemberService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author Cnegs
 * @Date 2022/1/21 1:13
 */
@RequestMapping("/report")
@RestController
public class ReportController {

    @Reference
    private MemberService memberService;

    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){
        //生成当前时间得前12个月，以及每个月的会员注册会员数量
        List<String> monthList = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        Calendar calendar =Calendar.getInstance();
        calendar.add(Calendar.MONTH,-12);//获得当前日期之前12个月的日期
        for (int i = 0; i < 12; i++) {
            //每循环一次就月份加一
            calendar.add(Calendar.MONTH,1);
            monthList.add(new SimpleDateFormat("yyyy.MM").format(calendar.getTime()));
        }
        map.put("months",monthList);
        List<Integer> memberCountList =new ArrayList<>();
        //查数据库，每月会员注册数量
        memberCountList = memberService.queryMemberCountByMonth(monthList);
        if(memberCountList!=null&&memberCountList.size()>0){
            map.put("memberCount",memberCountList);
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
        }else {
            return new Result(false,MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }
}
