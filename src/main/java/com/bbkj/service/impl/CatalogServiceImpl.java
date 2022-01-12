package com.bbkj.service.impl;


import com.bbkj.common.utils.Page;
import com.bbkj.dao.CatalogDao;
import com.bbkj.domain.Catalog;
import com.bbkj.service.CatalogService;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO
 * @date 2021/3/11 20:37
 */
@Service
public class CatalogServiceImpl implements CatalogService {
    @Autowired
    private CatalogDao catalogDao;

    @Override
    public Catalog findAdressById(Long id) {
        Catalog catalog = catalogDao.load(id);
        Hibernate.initialize(catalog);
        return catalog;
    }

    @Override
    public Serializable save(Catalog history) {
        return catalogDao.save(history);
    }

    @Override
    public void update(Catalog history) {
        catalogDao.update(history);
    }

    @Override
    public void remove(Catalog history) {
        catalogDao.remove(history);
    }

    @Override
    public void remove(long[] ids) {
        catalogDao.remove(ids);
    }

    @Override
    public List<Catalog> listAll() {
        return catalogDao.listAll();
    }

    @Override
    public List<Catalog> list(DetachedCriteria criteria) {
        return catalogDao.list(criteria);
    }

    @Override
    public Page pageList(DetachedCriteria criteria, int pageNo, int pageSize) {
        return catalogDao.pageList(criteria, pageNo, pageSize);
    }

    @Override
    public List<Catalog> findFather() {
        DetachedCriteria criteria = DetachedCriteria.forClass(Catalog.class)
                .add(Restrictions.eq("paren_id", 0l));
        return catalogDao.list(criteria);
    }

    @Override
    public List<Catalog> findChild(long fatherId) {
        DetachedCriteria criteria1 = DetachedCriteria.forClass(Catalog.class)
                .add(Restrictions.eq("paren_id", fatherId));
        return catalogDao.list(criteria1);
    }

    @Override
    public Page findFather(int pageNo, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Catalog.class)
                .add(Restrictions.eq("paren_id", 0l));
        return catalogDao.pageList(criteria, pageNo, pageSize);
    }

    @Override
    public Catalog findById(long id) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Catalog.class)
                .add(Property.forName("id").eq(id));
        List<Catalog> catalogs = catalogDao.list(criteria);
        return catalogs.size() > 0 ? catalogs.get(0) : null;
    }
}
