package com.bbkj.service;


import com.bbkj.common.utils.Page;
import com.bbkj.domain.Return;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.List;

public interface ReturnService {
    public Return findAdressById(Long id);

    public Serializable save(Return collect);

    public void update(Return collect);

    public void remove(Return collect);

    public void remove(long[] ids);

    public List<Return> listAll();

    public List<Return> list(DetachedCriteria criteria);

    public Page pageList(DetachedCriteria criteria,
                         final int pageNo, final int pageSize);
}
