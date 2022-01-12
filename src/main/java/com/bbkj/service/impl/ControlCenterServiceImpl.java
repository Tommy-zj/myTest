package com.bbkj.service.impl;


import com.bbkj.common.utils.Page;
import com.bbkj.dao.ControlCenterDao;
import com.bbkj.domain.ControlCenter;
import com.bbkj.service.ControlCenterService;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO
 * @date 2020/10/30 14:52
 */
@Service
public class ControlCenterServiceImpl implements ControlCenterService {
    @Autowired
    private ControlCenterDao controlCenterDAO;

    @Override
    public ControlCenter findAdressById(Long id) {
        return controlCenterDAO.load(id);
    }

    @Override
    public Serializable save(ControlCenter collect) {
        return controlCenterDAO.save(collect);
    }

    @Override
    public void update(ControlCenter collect) {
        controlCenterDAO.update(collect);
    }

    @Override
    public void remove(ControlCenter collect) {
        controlCenterDAO.remove(collect);
    }

    @Override
    public void remove(long[] ids) {
        controlCenterDAO.remove(ids);
    }

    @Override
    public List<ControlCenter> listAll() {
        return controlCenterDAO.listAll();
    }

    @Override
    public List<ControlCenter> list(DetachedCriteria criteria) {
        return controlCenterDAO.list(criteria);
    }

    @Override
    public Page pageList(DetachedCriteria criteria, int pageNo, int pageSize) {
        return controlCenterDAO.pageList(criteria, pageNo, pageSize);
    }


}
