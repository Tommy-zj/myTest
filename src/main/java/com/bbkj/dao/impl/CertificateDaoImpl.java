package com.bbkj.dao.impl;

import com.bbkj.common.genericDao.hibernate.GenericDaoImpl;
import com.bbkj.dao.CertificateDao;
import com.bbkj.domain.Certificate;
import org.springframework.stereotype.Repository;


@Repository
public class CertificateDaoImpl extends GenericDaoImpl<Certificate> implements CertificateDao {

}
