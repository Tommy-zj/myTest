package com.bbkj.service.impl;


import com.bbkj.common.utils.Page;
import com.bbkj.dao.DemandDao;
import com.bbkj.domain.Demand;
import com.bbkj.domain.Member;
import com.bbkj.service.DemandService;
import org.hibernate.Hibernate;
import org.hibernate.criterion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class DemandServiceImpl implements DemandService {
    @Autowired
    private DemandDao demandDAO;

    @Override
    public List findByHql(int status, int offset, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Demand.class);
        criteria.add(Property.forName("status").eq(status));
        Page page = demandDAO.pageList(criteria, offset, pageSize);
        List<Demand> demands = page.getPageList();
        return demands;
    }

    @Override
    public Demand findById(Long id) {
        Demand demand = demandDAO.load(id);
        Hibernate.initialize(demand);
        return demand;
    }

    @Override
    public Serializable save(Demand request) {
        return demandDAO.save(request);
    }

    @Override
    public void update(Demand request) {
        demandDAO.update(request);
    }

    @Override
    public void remove(Demand request) {
        demandDAO.remove(request);
    }

    @Override
    public void remove(long[] ids) {
        demandDAO.remove(ids);
    }

    @Override
    public List<Demand> listAll() {
        return demandDAO.listAll();
    }

    @Override
    public List<Demand> list(DetachedCriteria criteria) {
        return demandDAO.list(criteria);
    }

    @Override
    public Page pageList(DetachedCriteria criteria, int pageNo, int pageSize) {
        return demandDAO.pageList(criteria, pageNo, pageSize);
    }


    @Override
    public Number count(DetachedCriteria criteria) {
        return demandDAO.count(criteria);
    }

    @Override
    public Page findByPerson(long person_id, int status, int pageNo, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Demand.class);
        criteria.addOrder(Order.desc("id"));
        criteria.add(Restrictions.eq("person_id", person_id));
        criteria.add(Restrictions.eq("status", status));
        return demandDAO.pageList(criteria, pageNo, pageSize);
    }

    @Override
    public Page findByContent(String content, int pageNo, int pageSize) {
        DetachedCriteria criteriaRequest = DetachedCriteria.forClass(Demand.class)
                .add(Restrictions.eq("status", 2))
                .add(Property.forName("content").like(content, MatchMode.ANYWHERE));
        return demandDAO.pageList(criteriaRequest, pageNo, pageSize);
    }

    @Override
    public Page findDemandByCard(String which, String province_name, String city_name, String county_name, int whichpage, int pagesize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Demand.class);
        criteria.add(Restrictions.eq("status", 2));
        try {
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
                    criteria.add(Property.forName("county_name").like(city_name, MatchMode.ANYWHERE));
                    break;
                case "recent":
                    //小程序点击最新需求,五天的毫秒数=432000000
                    long five_days_ago = System.currentTimeMillis() - 432000000;
                    criteria.add(Restrictions.ge("create_time", five_days_ago));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Page page = demandDAO.pageList(criteria, whichpage, pagesize);
        return page;
    }

    @Override
    public Page findByRecent() {
        DetachedCriteria criteria = DetachedCriteria.forClass(Demand.class);
        criteria.add(Restrictions.eq("status", 2));
        long five_days_ago = System.currentTimeMillis() - 432000000;
        criteria.add(Restrictions.ge("create_time", five_days_ago));
        Page page = demandDAO.pageList(criteria, 1, 3);
        return page;
    }

    @Override
    public Demand findByDemandId(long demandId) {
        DetachedCriteria criteria1 = DetachedCriteria.forClass(Demand.class)
                .add(Restrictions.eq("id", demandId));
        List<Demand> demands = demandDAO.list(criteria1);
        return demands.size() > 0 ? demands.get(0) : null;
    }

    @Override
    public Number countByWhich(String name, String which) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Demand.class)
                .add(Restrictions.eq("status", 2));
        if (which.equals("all")) {
            return demandDAO.count(criteria);
        }
        criteria.add(Property.forName(which).like(name, MatchMode.ANYWHERE));
        return demandDAO.count(criteria);
    }

    @Override
    public Number countRecent() {
        DetachedCriteria criteria = DetachedCriteria.forClass(Demand.class);
        criteria.add(Restrictions.eq("status", 2));
        long five_days_ago = System.currentTimeMillis() - 432000000;
        criteria.add(Restrictions.ge("create_time", five_days_ago));
        return demandDAO.count(criteria);
    }

    @Override
    public List<Demand> count() {
        DetachedCriteria criteria = DetachedCriteria.forClass(Demand.class);
        List<Demand> demands = demandDAO.list(criteria);
        return demands.size() > 0 ? demands : null;
    }
}
