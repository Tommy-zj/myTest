package com.bbkj.service.impl;



import com.bbkj.common.utils.Page;
import com.bbkj.dao.ReturnDao;
import com.bbkj.domain.Return;
import com.bbkj.service.ReturnService;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO
 * @date 2020/11/16 9:53
 */
@Service
public class ReturnServiceImpl implements ReturnService {
    @Autowired
    private ReturnDao returnDAO;


    @Override
    public Return findAdressById(Long id) {
        return returnDAO.load(id);
    }

    @Override
    public Serializable save(Return collect) {
        return returnDAO.save(collect);
    }

    @Override
    public void update(Return collect) {
        returnDAO.update(collect);
    }

    @Override
    public void remove(Return collect) {
        returnDAO.remove(collect);
    }

    @Override
    public void remove(long[] ids) {
        returnDAO.remove(ids);
    }

    @Override
    public List<Return> listAll() {
        return returnDAO.listAll();
    }

    @Override
    public List<Return> list(DetachedCriteria criteria) {
        return returnDAO.list(criteria);
    }

    @Override
    public Page pageList(DetachedCriteria criteria, int pageNo, int pageSize) {
        return returnDAO.pageList(criteria, pageNo, pageSize);
    }
}
