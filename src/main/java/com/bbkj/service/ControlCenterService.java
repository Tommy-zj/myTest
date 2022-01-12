package com.bbkj.service;


import com.bbkj.common.utils.Page;
import com.bbkj.domain.ControlCenter;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.List;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO
 * @date 2020/10/30 14:49
 */
public interface ControlCenterService {
    public ControlCenter findAdressById(Long id);

    public Serializable save(ControlCenter collect);

    public void update(ControlCenter collect);

    public void remove(ControlCenter collect);

    public void remove(long[] ids);

    public List<ControlCenter> listAll();

    public List<ControlCenter> list(DetachedCriteria criteria);

    public Page pageList(DetachedCriteria criteria,
                         final int pageNo, final int pageSize);

}
