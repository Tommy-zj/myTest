package com.bbkj.service.impl;


import com.bbkj.common.utils.Page;
import com.bbkj.dao.ProductExtraDao;
import com.bbkj.domain.ProductExtra;
import com.bbkj.service.ProductExtraService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO
 * @date 2020/12/16 9:36
 */
@Service
public class ProductExtraServiceImpl implements ProductExtraService {
    @Autowired
    private ProductExtraDao productExtraDAO;

    @Override
    public ProductExtra findById(Long id) {
        return productExtraDAO.load(id);
    }

    @Override
    public Serializable save(ProductExtra product) {
        return productExtraDAO.save(product);
    }

    @Override
    public void update(ProductExtra product) {
        productExtraDAO.update(product);
    }

    @Override
    public void remove(ProductExtra product) {
        productExtraDAO.remove(product);
    }

    @Override
    public void remove(long[] ids) {
        productExtraDAO.remove(ids);
    }

    @Override
    public List<ProductExtra> listAll() {
        return productExtraDAO.listAll();
    }

    @Override
    public List<ProductExtra> list(DetachedCriteria criteria) {
        return productExtraDAO.list(criteria);
    }

    @Override
    public Page pageList(DetachedCriteria criteria, int pageNo, int pageSize) {
        return productExtraDAO.pageList(criteria, pageNo, pageSize);
    }

    @Override
    public ProductExtra findByProductId(long product_id) {
        DetachedCriteria criteria11 = DetachedCriteria.forClass(ProductExtra.class)
                .add(Restrictions.eq("product_id", product_id))
                .addOrder(Order.desc("id"));
        List<ProductExtra> productExtras = productExtraDAO.list(criteria11);
        return productExtras.size() > 0 ? productExtras.get(0) : null;
    }

    @Override
    public List<ProductExtra> findByProductIdAll(long product_id) {
        DetachedCriteria criteria11 = DetachedCriteria.forClass(ProductExtra.class)
                .add(Restrictions.eq("product_id", product_id))
                .addOrder(Order.desc("id"));
        List<ProductExtra> productExtras = productExtraDAO.list(criteria11);
        return productExtras.size() > 0 ? productExtras : null;
    }
}
