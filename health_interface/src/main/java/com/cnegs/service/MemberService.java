package com.cnegs.service;

import com.cnegs.pojo.Member;

import java.util.List;

/**
 * @Author Cnegs
 * @Date 2022/1/17 22:50
 */
public interface MemberService {

    Member findById(String telephone);

    void add(Member member);

    List<Integer> queryMemberCountByMonth(List<String> monthList);
}
