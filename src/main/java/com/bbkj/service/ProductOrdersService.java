package com.bbkj.service;


import com.bbkj.common.utils.Page;
import com.bbkj.domain.ShopOrders;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.List;

public interface ProductOrdersService {
    public ShopOrders findAdressById(Long id);

    public Serializable save(ShopOrders collect);

    public void update(ShopOrders collect);

    public void remove(ShopOrders collect);

    public void remove(long[] ids);

    public List<ShopOrders> listAll();

    public List<ShopOrders> list(DetachedCriteria criteria);

    public Page pageList(DetachedCriteria criteria,
                         final int pageNo, final int pageSize);

}
