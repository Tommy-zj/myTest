package com.bbkj.service;


import com.bbkj.common.utils.Page;
import com.bbkj.domain.Address;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.List;

public interface AdressService {
     Address findAdressById(Long id);

     Serializable save(Address adress);

     void update(Address adress);

     void remove(Address adress);

     void remove(long[] ids);

     List<Address> listAll();

     List<Address> list(DetachedCriteria criteria);

     Page pageList(DetachedCriteria criteria,
                         final int pageNo, final int pageSize);

     Address findDefaultAdress(long person_id);

    List<Address> findAllDefaultAdress(long person_id);

     Page pagelist(int pageNo, int pageSize);

    Page findAllAdressByPersonId(long person_id, int pageNo, int pageSize);
}
