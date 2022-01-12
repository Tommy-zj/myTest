package com.bbkj.service;


import com.bbkj.common.utils.Page;
import com.bbkj.domain.History;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.List;

public interface HistoryService {
    public History findAdressById(Long id);

    public Serializable save(History history);

    public void update(History history);

    public void remove(History history);

    public void remove(long[] ids);

    public List<History> listAll();

    public List<History> list(DetachedCriteria criteria);

    public Page pageList(DetachedCriteria criteria,
                         final int pageNo, final int pageSize);
    public List<History> find(long person_id, long product_id);


}
