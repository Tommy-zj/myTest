package com.bbkj.dao.impl;

import com.bbkj.common.genericDao.hibernate.GenericDaoImpl;
import com.bbkj.dao.ProductOrdersDao;
import com.bbkj.domain.ShopOrders;
import org.springframework.stereotype.Repository;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO
 * @date 2020/11/11 10:10
 */
@Repository
public class ProductOrdersDaoImpl extends GenericDaoImpl<ShopOrders> implements ProductOrdersDao {
}
