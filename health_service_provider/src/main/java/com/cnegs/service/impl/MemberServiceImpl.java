package com.cnegs.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.cnegs.dao.MemberDao;
import com.cnegs.pojo.Member;
import com.cnegs.service.MemberService;
import com.cnegs.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Cnegs
 * @Date 2022/1/17 23:12
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    @Override
    public Member findById(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        //注意保存密码时要加密处理
        String password = member.getPassword();
        if(password!=null){
            password = MD5Utils.md5(password);
            member.setPassword(password);
        }
        memberDao.add(member);
    }

    @Override
    public List<Integer> queryMemberCountByMonth(List<String> monthList) {
        List<Integer> counts = new ArrayList<>();
        for (String s : monthList) {
            String replace = s.replace(".", "-");
            String startTime = replace+"-01";
            String endTime = replace+"-31";
            Map<String,String> map = new HashMap<>();
            map.put("startTime",startTime);
            map.put("endTime",endTime);
            Integer memberCount = memberDao.findMemberCountBetweenDate(map);
            counts.add(memberCount);
        }

        return counts;
    }
}
