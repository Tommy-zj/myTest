package com.bbkj.service;


import com.bbkj.common.utils.Page;
import com.bbkj.domain.ReturnImg;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.List;

public interface ReturnImgService {
    public ReturnImg findAdressById(Long id);

    public Serializable save(ReturnImg collect);

    public void update(ReturnImg collect);

    public void remove(ReturnImg collect);

    public void remove(long[] ids);

    public List<ReturnImg> listAll();

    public List<ReturnImg> list(DetachedCriteria criteria);

    public Page pageList(DetachedCriteria criteria,
                         final int pageNo, final int pageSize);
}
