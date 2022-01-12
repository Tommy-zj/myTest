package com.bbkj.service;

import com.bbkj.common.utils.Page;
import com.bbkj.domain.Catalog;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.List;

public interface CatalogService {
    Catalog findAdressById(Long id);

    Serializable save(Catalog history);

    void update(Catalog history);

    void remove(Catalog history);

    void remove(long[] ids);

    List<Catalog> listAll();

    List<Catalog> list(DetachedCriteria criteria);

    Page pageList(DetachedCriteria criteria,
                  final int pageNo, final int pageSize);

    List<Catalog> findFather();

    List<Catalog> findChild(long fatherId);

    Page findFather(int pageNo, int pageSize);

    Catalog findById(long id);
}
