package com.bbkj.service.impl;


import com.bbkj.common.utils.Page;
import com.bbkj.dao.MemberDao;
import com.bbkj.domain.Member;
import com.bbkj.service.MemberService;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;

    @Override
    public Member findById(Long id) {
        Member member = memberDao.load(id);
        Hibernate.initialize(member);
        return member;
    }

    @Override
    public Member findByOpenid(String openid) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Member.class)
                .add(Property.forName("openid").eq(openid));
        List<Member> members = memberDao.list(criteria);
        return members.size() == 1 ? members.get(0) : null;
    }

    @Override
    public Serializable save(Member member) {
        return memberDao.save(member);
    }

    @Override
    public void update(Member member) {
        memberDao.update(member);
    }

    @Override
    public void remove(Member member) {
        memberDao.remove(member);
    }

    @Override
    public void remove(long[] ids) {
        memberDao.remove(ids);
    }

    @Override
    public List<Member> listAll() {
        return memberDao.listAll();
    }

    @Override
    public List<Member> list(DetachedCriteria criteria) {
        return memberDao.list(criteria);
    }

    @Override
    public Page pageList(DetachedCriteria criteria, int pageNo, int pageSize) {
        return memberDao.pageList(criteria, pageNo, pageSize);
    }

    @Override
    public Page pageList(int pageNo, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Member.class);
        Page page = memberDao.pageList(criteria, pageNo, pageSize);
        return page;
    }

    @Override
    public Object query(String hql) {
        return memberDao.query(hql);
    }

    @Override
    public Number count(DetachedCriteria criteria) {
        return memberDao.count(criteria);
    }

    @Override
    public Page findByCondition(int pageNo, int pageSize, String which, String province_name, String city_name, String county_name) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Member.class)
                .add(Restrictions.isNotNull("mail"));
        switch (which) {
            case "province_name":
                //小程序点击省需求
                criteria.add(Property.forName("province_name").like(province_name, MatchMode.ANYWHERE));
                break;
            case "city_name":
                //小程序点击市需求
                criteria.add(Property.forName("city_name").like(city_name, MatchMode.ANYWHERE));
                break;
            case "county_name":
                //小程序点击区需求或附近需求
                criteria.add(Property.forName("county_name").like(county_name, MatchMode.ANYWHERE));
                break;
            case "recent":
                //小程序点击最新需求,五天的毫秒数=432000000
                long five_days_ago = System.currentTimeMillis() - 432000000;
                criteria.add(Restrictions.gt("create_time", five_days_ago));
                break;
        }
        Page page = memberDao.pageList(criteria, pageNo, pageSize);
        return page;
    }

    @Override
    public List<Member> count() {
        DetachedCriteria criteria = DetachedCriteria.forClass(Member.class);
        List<Member> members = memberDao.list(criteria);
        return members.size() > 0 ? members : null;
    }

    @Override
    public Number countByWhich(String name, String which) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Member.class)
                .add(Restrictions.isNotNull("mail"));
        if (which.equals("all")) {
            return memberDao.count(criteria);
        }
        criteria.add(Property.forName(which).like(name, MatchMode.ANYWHERE));
        return memberDao.count(criteria);
    }

    @Override
    public Number countRecent() {
        DetachedCriteria criteria = DetachedCriteria.forClass(Member.class)
                .add(Restrictions.gt("create_time", System.currentTimeMillis() - 432000000))
                .add(Restrictions.isNotNull("mail"));
        return memberDao.count(criteria);
    }


    /**
     * @param phone
     * @param password
     * @return com.bbkj.domain.Member
     * @author JJ
     * @Description 手机端验证登录
     * @Date 2021/11/26 10:02
     **/
    @Override
    public Member phoneLogin(Long phone, String password) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Member.class)
                .add(Restrictions.eq("phone", phone));
        List<Member> members = memberDao.list(criteria);
        if (members.size() > 0) {
            // 找到了同名的用户，并验证密码
            Member member = members.get(0);
            if (member.getPassword() == password || member.getPassword().equals(password)) {
                // 密码正确
                return member;
            }
            //密码错误
            return null;
        }
        return null;
    }

    @Override
    public Boolean checkRegister(Long phone) {
        List<Member> members = memberDao.list(DetachedCriteria.forClass(Member.class)
                .add(Restrictions.eq("phone", phone)));
        if (members.size() > 0) {
            return false;
        }
        return true;
    }

}
