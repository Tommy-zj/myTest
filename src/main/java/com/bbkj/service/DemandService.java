package com.bbkj.service;

import com.bbkj.common.utils.Page;
import com.bbkj.domain.Demand;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.List;

public interface DemandService {
    Demand findById(Long id);

    Serializable save(Demand request);

    void update(Demand request);

    void remove(Demand request);

    void remove(long[] ids);

    List<Demand> listAll();

    List<Demand> list(DetachedCriteria criteria);

    Page pageList(DetachedCriteria criteria,
                  final int pageNo, final int pageSize);

    List findByHql(int status, int offset, int pageSize);

    Number count(DetachedCriteria criteria);


    Page findByPerson(long person_id, int status, int pageNo, int pageSize);

    Page findByContent(String content, int pageNo, int pageSize);

    Page findDemandByCard(String which, String province_name, String city_name, String county_name, int whichpage, int pagesize);

    Page findByRecent();

    Demand findByDemandId(long demandId);

    Number countByWhich(String name, String which);

    Number countRecent();

    List<Demand> count();
}
