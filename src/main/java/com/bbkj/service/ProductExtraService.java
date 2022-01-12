package com.bbkj.service;


import com.bbkj.common.utils.Page;
import com.bbkj.domain.ProductExtra;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.List;

public interface ProductExtraService {
    public ProductExtra findById(Long id);

    public Serializable save(ProductExtra product);

    public void update(ProductExtra product);

    public void remove(ProductExtra product);

    public void remove(long[] ids);

    public List<ProductExtra> listAll();

    public List<ProductExtra> list(DetachedCriteria criteria);

    public Page pageList(DetachedCriteria criteria,
                         final int pageNo, final int pageSize);

    public ProductExtra findByProductId(long product_id);
    public List<ProductExtra> findByProductIdAll(long product_id);
}
