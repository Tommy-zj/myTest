package com.bbkj.service.impl;


import com.bbkj.common.utils.Page;
import com.bbkj.dao.CertificateDao;
import com.bbkj.domain.Certificate;
import com.bbkj.service.CertificateService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class CertificateServiceImpl implements CertificateService {
    @Autowired
    private CertificateDao certificateDAO;

    @Override
    public Certificate findById(Long id) {
        return certificateDAO.load(id);
    }

    @Override
    public Serializable save(Certificate certificate) {
        return certificateDAO.save(certificate);
    }

    @Override
    public void update(Certificate certificate) {
        certificateDAO.update(certificate);
    }

    @Override
    public void remove(Certificate certificate) {
        certificateDAO.remove(certificate);
    }

    @Override
    public void remove(long[] ids) {
        certificateDAO.remove(ids);
    }

    @Override
    public List<Certificate> list(DetachedCriteria criteria) {
        return certificateDAO.list(criteria);
    }


    @Override
    public Page pageList(DetachedCriteria criteria, int pageNo, int pageSize) {
        return certificateDAO.pageList(criteria, pageNo, pageSize);
    }

    @Override
    public List findByPerson(long personId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Certificate.class)
                .add(Property.forName("person_id").eq(personId));
        List<Certificate> certificates = certificateDAO.list(criteria);
        return certificates.size() > 0 ? certificates : null;
    }

    @Override
    public List<Certificate> findByPersonIdPass(long person_id) {
        DetachedCriteria CCriteria = DetachedCriteria.forClass(Certificate.class)
                .add(Restrictions.eq("person_id", person_id))
                .add(Restrictions.eq("state", 1));
        List<Certificate> certificates = certificateDAO.list(CCriteria);
        return certificates.size() > 0 ? certificates : null;
    }

    @Override
    public Page findNotReview(int pageNo,int pageSize) {
        DetachedCriteria criteria1 = DetachedCriteria.forClass(Certificate.class)
                .add(Restrictions.eq("state", 0))
                .add(Restrictions.isNull("remark"));
        Page page = certificateDAO.pageList(criteria1, pageNo, pageSize);
        return page;
    }
}
