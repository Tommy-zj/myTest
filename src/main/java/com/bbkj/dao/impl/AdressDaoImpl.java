package com.bbkj.dao.impl;


import com.bbkj.common.genericDao.hibernate.GenericDaoImpl;
import com.bbkj.dao.AddressDao;
import com.bbkj.domain.Address;
import org.springframework.stereotype.Repository;

@Repository
public class AdressDaoImpl extends GenericDaoImpl<Address> implements AddressDao {

}
