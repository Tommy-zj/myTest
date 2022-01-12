package com.bbkj.service.impl;


import com.bbkj.common.utils.Page;
import com.bbkj.dao.CollectDao;
import com.bbkj.domain.Collect;
import com.bbkj.service.CollectService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class CollectServiceImpl implements CollectService {
    @Autowired
    private CollectDao collectDAO;

    @Override
    public Collect findAdressById(Long id) {
        return collectDAO.load(id);
    }

    @Override
    public Serializable save(Collect collect) {
        return collectDAO.save(collect);
    }

    @Override
    public void update(Collect collect) {
        collectDAO.update(collect);
    }

    @Override
    public void remove(Collect collect) {
        collectDAO.remove(collect);
    }

    @Override
    public void remove(long[] ids) {
        collectDAO.remove(ids);
    }

    @Override
    public List<Collect> listAll() {
        return collectDAO.listAll();
    }

    @Override
    public List<Collect> list(DetachedCriteria criteria) {
        return collectDAO.list(criteria);
    }

    @Override
    public Page pageList(DetachedCriteria criteria, int pageNo, int pageSize) {
        return collectDAO.pageList(criteria, pageNo, pageSize);
    }


    /**
     * @author JJ
     * @Description 判断此人是否收藏了此商品
     * @Date 2021/4/11 17:26
     * @Param [product_id, person_id]
     * @Return boolean
     **/
    @Override
    public boolean isCollect(long product_id, long person_id) {
        DetachedCriteria criteria1 = DetachedCriteria.forClass(Collect.class)
                .add(Property.forName("person_id").eq(person_id))
                .add(Property.forName("product_id").eq(product_id));
        Number number = collectDAO.count(criteria1);
        if (number.equals(0l)) {
            return false;
        }
        return true;
    }

    @Override
    public List<Collect> find(long person_id, long product_id) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Collect.class)
                .add(Restrictions.eq("product_id", product_id))
                .add(Restrictions.eq("person_id", person_id));
        List<Collect> collects = collectDAO.list(criteria);
        return collects;
    }

    @Override
    public List<Collect> findByPersonId(long person_id) {
        DetachedCriteria criteria1 = DetachedCriteria.forClass(Collect.class);
        criteria1.add(Restrictions.eq("person_id", person_id));
        List<Collect> collects = collectDAO.list(criteria1);
        return collects.size() > 0 ? collects : null;
    }

    @Override
    public Number count(long person_id) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Collect.class)
                .add(Property.forName("person_id").eq(person_id));
        return collectDAO.count(criteria);
    }

}
