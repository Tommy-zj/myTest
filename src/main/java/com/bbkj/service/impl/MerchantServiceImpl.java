package com.bbkj.service.impl;

import com.bbkj.common.utils.Page;
import com.bbkj.dao.MerchantDao;
import com.bbkj.domain.Merchant;
import com.bbkj.service.MerchantService;
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
 * @date 2021/4/22 11:23
 */
@Service
public class MerchantServiceImpl implements MerchantService {
    @Autowired
    private MerchantDao merchantDao;

    @Override
    public Merchant findById(Long id) {
        Merchant merchant = merchantDao.load(id);
        Hibernate.initialize(merchant);
        return merchant;
    }


    @Override
    public Serializable save(Merchant member) {
        return merchantDao.save(member);
    }

    @Override
    public void update(Merchant member) {
        merchantDao.update(member);
    }

    @Override
    public void remove(Merchant member) {
        merchantDao.remove(member);
    }

    @Override
    public void remove(long[] ids) {
        merchantDao.remove(ids);
    }

    @Override
    public List<Merchant> listAll() {
        return merchantDao.listAll();
    }

    @Override
    public List<Merchant> list(DetachedCriteria criteria) {
        return merchantDao.list(criteria);
    }

    @Override
    public Page pageList(DetachedCriteria criteria, int pageNo, int pageSize) {
        return merchantDao.pageList(criteria, pageNo, pageSize);
    }


    @Override
    public Object query(String hql) {
        return merchantDao.query(hql);
    }

    @Override
    public Number count(DetachedCriteria criteria) {
        return merchantDao.count(criteria);
    }

    @Override
    public Merchant findByPhone(long phone) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Merchant.class)
                .add(Restrictions.eq("phone", phone));
        List<Merchant> merchants = merchantDao.list(criteria);
        return merchants.size() > 0 ? merchants.get(0) : null;
    }
}
