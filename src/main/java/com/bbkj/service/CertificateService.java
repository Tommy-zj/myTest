package com.bbkj.service;


import com.bbkj.common.utils.Page;
import com.bbkj.domain.Certificate;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.List;

public interface CertificateService {
    public Certificate findById(Long id);

    public Serializable save(Certificate certificate);

    public void update(Certificate certificate);

    public void remove(Certificate certificate);

    public void remove(long[] ids);

    public List<Certificate> list(DetachedCriteria criteria);


    public Page pageList(DetachedCriteria criteria,
                         final int pageNo, final int pageSize);

    public List findByPerson(long personId);

    List<Certificate> findByPersonIdPass(long person_id);

    Page findNotReview(int pageNo, int pageSize);
}
