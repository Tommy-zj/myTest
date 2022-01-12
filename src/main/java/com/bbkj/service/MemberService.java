package com.bbkj.service;


import com.bbkj.common.utils.Page;
import com.bbkj.domain.Member;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.List;

public interface MemberService {
    Member findById(Long id);

    Member findByOpenid(String openid);

    Serializable save(Member member);

    void update(Member member);

    void remove(Member member);

    void remove(long[] ids);

    List<Member> listAll();

    List<Member> list(DetachedCriteria criteria);

    Page pageList(DetachedCriteria criteria,
                  final int pageNo, final int pageSize);

    Page pageList(int pageNo, int pageSize);

    Object query(String hql);

    Number count(DetachedCriteria criteria);

    Page findByCondition(int pageNo, int pageSize, String which, String province_name, String city_name, String county_name);

    List<Member> count();

    Number countByWhich(String name, String which);

    Number countRecent();

    Member phoneLogin(Long phone, String password);

    Boolean checkRegister(Long phone);
}
