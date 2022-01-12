package com.bbkj.service;

import com.bbkj.common.utils.Page;
import com.bbkj.domain.Merchant;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.List;

public interface MerchantService {
    Merchant findById(Long id);

    Serializable save(Merchant member);

    void update(Merchant member);

    void remove(Merchant member);

    void remove(long[] ids);

    List<Merchant> listAll();

    List<Merchant> list(DetachedCriteria criteria);

    Page pageList(DetachedCriteria criteria,
                  final int pageNo, final int pageSize);


    Object query(String hql);

    Number count(DetachedCriteria criteria);

    Merchant findByPhone(long phone);
}
