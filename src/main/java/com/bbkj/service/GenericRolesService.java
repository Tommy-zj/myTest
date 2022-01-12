package com.bbkj.service;


import com.bbkj.common.utils.Page;
import com.bbkj.domain.GenericRoles;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.List;

public interface GenericRolesService {
    public List<GenericRoles> list(DetachedCriteria criteria);

    public GenericRoles findAdressById(Long id);

    public Serializable save(GenericRoles user);

    public void update(GenericRoles user);

    public void remove(GenericRoles user);

    public void remove(long[] ids);

    public List<GenericRoles> listAll();


    public Page pageList(DetachedCriteria criteria,
                         final int pageNo, final int pageSize);
}
