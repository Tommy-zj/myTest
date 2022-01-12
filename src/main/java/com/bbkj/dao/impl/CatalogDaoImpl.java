package com.bbkj.dao.impl;

import com.bbkj.common.genericDao.hibernate.GenericDaoImpl;
import com.bbkj.dao.CatalogDao;
import com.bbkj.domain.Catalog;
import org.springframework.stereotype.Repository;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO
 * @date 2021/3/11 20:33
 */
@Repository
public class CatalogDaoImpl extends GenericDaoImpl<Catalog> implements CatalogDao {
}
