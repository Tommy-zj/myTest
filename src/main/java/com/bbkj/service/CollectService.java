package com.bbkj.service;

import com.bbkj.common.utils.Page;
import com.bbkj.domain.Collect;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.List;

public interface CollectService {
    public Collect findAdressById(Long id);

    public Serializable save(Collect collect);

    public void update(Collect collect);

    public void remove(Collect collect);

    public void remove(long[] ids);

    public List<Collect> listAll();

    public List<Collect> list(DetachedCriteria criteria);

    public Page pageList(DetachedCriteria criteria,
                         final int pageNo, final int pageSize);

    public boolean isCollect(long product_id, long person_id);

    public List<Collect> find(long person_id, long product_id);

    List<Collect> findByPersonId(long person_id);

    Number count(long person_id);
}
