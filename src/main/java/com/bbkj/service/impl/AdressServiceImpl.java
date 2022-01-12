package com.bbkj.service.impl;

import com.bbkj.common.utils.Page;
import com.bbkj.dao.AddressDao;
import com.bbkj.domain.Address;
import com.bbkj.service.AdressService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
@Scope("prototype")
public class AdressServiceImpl implements AdressService {
    @Autowired
    private AddressDao addressDao;


    @Override
    public Address findAdressById(Long id) {
        return addressDao.load(id);
    }

    @Override
    public Serializable save(Address adress) {
        return addressDao.save(adress);
    }

    @Override
    public void update(Address adress) {
        addressDao.update(adress);
    }

    @Override
    public void remove(Address adress) {
        addressDao.remove(adress);
    }

    @Override
    public void remove(long[] ids) {
        addressDao.remove(ids);
    }

    @Override
    public List<Address> listAll() {
        return addressDao.listAll();
    }

    @Override
    public List<Address> list(DetachedCriteria criteria) {
        return addressDao.list(criteria);
    }

    @Override
    public Page pageList(DetachedCriteria criteria, int pageNo, int pageSize) {
        return addressDao.pageList(criteria, pageNo, pageSize);
    }

    /**
     * @author JJ
     * @Description 找默认的地址，没有返回找到的第一个
     * @Date 2021/4/12 16:24
     * @Param [person_id]
     * @Return com.beped.member.domain.Address
     **/
    @Override
    public Address findDefaultAdress(long person_id) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Address.class);
        criteria.add(Restrictions.eq("person_id", person_id));
        criteria.add(Restrictions.eq("checks", 1));
        List<Address> adress = addressDao.list(criteria);
        if (adress.size() > 0) {
            return adress.get(0);
        }
        DetachedCriteria criteria1 = DetachedCriteria.forClass(Address.class)
                .add(Restrictions.eq("person_id", person_id))
                .addOrder(Order.desc("id"));
        List<Address> address1 = addressDao.list(criteria1);
        if (address1.size() > 0) {
            return address1.get(0);
        }
        return null;
    }

    @Override
    public List<Address> findAllDefaultAdress(long person_id) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Address.class)
                .add(Restrictions.eq("person_id", person_id))
                .add(Restrictions.eq("checks", 1));
        return addressDao.list(criteria);
    }

    @Override
    public Page pagelist(int pageNo, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Address.class);
        Page page = addressDao.pageList(criteria, pageNo, pageSize);
        return page;
    }

    @Override
    public Page findAllAdressByPersonId(long person_id, int pageNo, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Address.class)
                .addOrder(Order.desc("id"))
                .add(Restrictions.eq("person_id", person_id));
        Page page = addressDao.pageList(criteria, pageNo, pageSize);
        return page;
    }
}
