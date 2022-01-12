package com.bbkj.service;

import com.bbkj.common.utils.Page;
import com.bbkj.domain.Product;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.List;

public interface ProductService {
    Product findById(Long id);

    Serializable save(Product product);

    void update(Product product);

    void remove(Product product);

    void remove(long[] ids);

    List<Product> listAll();

    List<Product> list(DetachedCriteria criteria);

    Page pageList(DetachedCriteria criteria,
                  final int pageNo, final int pageSize);

    Page findByName(String name, int pageNo, int pageSize);

    Page findByCatalog(long second_catalog_id, long first_catalog_id, int pageNo, int pageSize);

    Page findByCatalogWx(long second_catalog_id, int pageNo, int pageSize);

    Page findByRecommend(int recommendId, int pageNo, int pageSize);

    Page findPersonProduct(long person_id, int pageNo, int pageSize, int status);

    Page findReviewProduct(int pageNo, int pageSize);


}
