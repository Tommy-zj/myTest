package com.bbkj.dao.impl;


import com.bbkj.common.genericDao.hibernate.GenericDaoImpl;
import com.bbkj.dao.ProductDao;
import com.bbkj.domain.Product;
import org.springframework.stereotype.Repository;

@Repository
public class ProductDaoImpl extends GenericDaoImpl<Product> implements ProductDao {
}
