package com.bbkj.service.impl;


import com.bbkj.common.utils.Page;
import com.bbkj.dao.GenericRolesDao;
import com.bbkj.domain.GenericRoles;
import com.bbkj.service.GenericRolesService;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO
 * @date 2021/1/18 18:03
 */
@Service
public class GenericRolesServiceImpl implements GenericRolesService {
    @Autowired
    private GenericRolesDao genericRolesDAO;

    @Override
    public List<GenericRoles> list(DetachedCriteria criteria) {
        return genericRolesDAO.list(criteria);
    }

    @Override
    public GenericRoles findAdressById(Long id) {
        return genericRolesDAO.load(id);
    }

    @Override
    public Serializable save(GenericRoles user) {
        return genericRolesDAO.save(user);
    }

    @Override
    public void update(GenericRoles user) {
        genericRolesDAO.update(user);
    }

    @Override
    public void remove(GenericRoles user) {
        genericRolesDAO.remove(user);
    }

    @Override
    public void remove(long[] ids) {
        genericRolesDAO.remove(ids);
    }

    @Override
    public List<GenericRoles> listAll() {
        return genericRolesDAO.listAll();
    }

    @Override
    public Page pageList(DetachedCriteria criteria, int pageNo, int pageSize) {
        return genericRolesDAO.pageList(criteria, pageNo, pageSize);
    }
}
