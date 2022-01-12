package com.bbkj.dao.impl;


import com.bbkj.common.genericDao.hibernate.GenericDaoImpl;
import com.bbkj.dao.CollectDao;
import com.bbkj.domain.Collect;
import org.springframework.stereotype.Repository;

@Repository
public class CollectDaoImpl extends GenericDaoImpl<Collect> implements CollectDao {
}
