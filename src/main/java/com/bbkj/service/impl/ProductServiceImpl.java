package com.bbkj.service.impl;


import com.bbkj.common.utils.Page;
import com.bbkj.dao.ProductDao;
import com.bbkj.domain.Product;
import com.bbkj.service.ProductService;
import org.hibernate.Hibernate;
import org.hibernate.criterion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDAO;

    @Override
    public Product findById(Long id) {
        Product product = productDAO.load(id);
        Hibernate.initialize(product);
        return product;
    }

    @Override
    public Serializable save(Product product) {
        return productDAO.save(product);
    }

    @Override
    public void update(Product product) {
        productDAO.update(product);
    }

    @Override
    public void remove(Product product) {
        productDAO.remove(product);

    }

    @Override
    public void remove(long[] ids) {
        productDAO.remove(ids);

    }

    @Override
    public List<Product> listAll() {
        return productDAO.listAll();
    }

    @Override
    public List<Product> list(DetachedCriteria criteria) {
        return productDAO.list(criteria);
    }

    @Override
    public Page pageList(DetachedCriteria criteria, int pageNo, int pageSize) {
        return productDAO.pageList(criteria, pageNo, pageSize);
    }

    @Override
    public Page findByName(String name, int pageNo, int pageSize) {
        DetachedCriteria criteriaShop = DetachedCriteria.forClass(Product.class)
                .add(Property.forName("name").like(name, MatchMode.ANYWHERE))
                .add(Restrictions.eq("status", 1))
                .addOrder(Order.desc("id"));
        return productDAO.pageList(criteriaShop, pageNo, pageSize);
    }

    @Override
    public Page findByCatalog(long second_catalog_id, long first_catalog_id, int pageNo, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Product.class)
                .add(Restrictions.eq("status", 1))
                .addOrder(Order.desc("id"));
        if (second_catalog_id > 0l) {
            //查找全部的商品
            //log.info("这里需要全部商品信息");
            //按二级分类查找商品
            criteria.add(Restrictions.eq("second_catalog_id", second_catalog_id));
        }
        if (first_catalog_id > 0l) {
            criteria.add(Restrictions.eq("first_catalog_id", first_catalog_id));
        }
        //page为取出的那页数据
        return productDAO.pageList(criteria, pageNo, pageSize);
    }

    @Override
    public Page findByCatalogWx(long second_catalog_id, int pageNo, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Product.class)
                .addOrder(Order.desc("id"))
                .add(Restrictions.eq("status", 1))
                .add(Restrictions.eq("disable", false));

        if (second_catalog_id > 0) {
            //查找全部的商品
            //log.info("这里需要全部商品信息");
            //按二级分类查找商品
            criteria.add(Restrictions.eq("second_catalog_id", second_catalog_id));
        }
        //page为取出的那页数据
        return productDAO.pageList(criteria, pageNo, pageSize);
    }

    @Override
    public Page findByRecommend(int recommendId, int pageNo, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Product.class)
                .add((Restrictions.eq("recommend", recommendId)))
                .addOrder(Order.desc("id"))
                .add(Restrictions.eq("status", 1))
                .add(Restrictions.eq("disable", false));
        return productDAO.pageList(criteria, pageNo, pageSize);
    }

    @Override
    public Page findPersonProduct(long person_id, int pageNo, int pageSize, int status) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Product.class)
                .add(Property.forName("person_id").eq(person_id));
        if (status != 3) {
            criteria.add(Property.forName("status").eq(status));
        }
        Page page = productDAO.pageList(criteria, pageNo, pageSize);
        return page;
    }

    @Override
    public Page findReviewProduct(int pageNo, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Product.class)
                .add(Property.forName("status").eq(0));
        Page page = productDAO.pageList(criteria, pageNo, pageSize);
        return page;
    }

}
