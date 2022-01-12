package com.bbkj.service.impl;


import com.bbkj.common.utils.Page;
import com.bbkj.dao.ProductOrdersDao;
import com.bbkj.domain.ShopOrders;
import com.bbkj.service.ProductOrdersService;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO
 * @date 2020/11/11 10:19
 */
@Service
public class ProductOrdersServiceImpl implements ProductOrdersService {
    @Autowired
    private ProductOrdersDao productOrdersDAO;

    @Override
    public ShopOrders findAdressById(Long id) {
        return productOrdersDAO.load(id);
    }

    @Override
    public Serializable save(ShopOrders collect) {
        return productOrdersDAO.save(collect);
    }

    @Override
    public void update(ShopOrders collect) {
        productOrdersDAO.update(collect);
    }

    @Override
    public void remove(ShopOrders collect) {
        productOrdersDAO.remove(collect);
    }

    @Override
    public void remove(long[] ids) {
        productOrdersDAO.remove(ids);
    }

    @Override
    public List<ShopOrders> listAll() {
        return productOrdersDAO.listAll();
    }

    @Override
    public List<ShopOrders> list(DetachedCriteria criteria) {
        return productOrdersDAO.list(criteria);
    }

    @Override
    public Page pageList(DetachedCriteria criteria, int pageNo, int pageSize) {
        return productOrdersDAO.pageList(criteria, pageNo, pageSize);
    }
}
