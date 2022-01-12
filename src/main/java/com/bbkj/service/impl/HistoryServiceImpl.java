package com.bbkj.service.impl;


import com.bbkj.common.utils.Page;
import com.bbkj.dao.HistoryDao;
import com.bbkj.domain.History;
import com.bbkj.service.HistoryService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO
 * @date 2020/9/14 16:29
 */
@Service
public class HistoryServiceImpl implements HistoryService {
    @Autowired
    private HistoryDao historyDAO;

    public void setHistoryDAO(HistoryDao historyDAO) {
        this.historyDAO = historyDAO;
    }

    @Override
    public History findAdressById(Long id) {
        return historyDAO.load(id);
    }

    @Override
    public Serializable save(History history) {
        return historyDAO.save(history);
    }

    @Override
    public void update(History history) {
        historyDAO.update(history);
    }

    @Override
    public void remove(History history) {
        historyDAO.remove(history);
    }

    @Override
    public void remove(long[] ids) {
        historyDAO.remove(ids);
    }

    @Override
    public List<History> listAll() {
        return historyDAO.listAll();
    }

    @Override
    public List<History> list(DetachedCriteria criteria) {
        return historyDAO.list(criteria);
    }

    @Override
    public Page pageList(DetachedCriteria criteria, int pageNo, int pageSize) {
        return historyDAO.pageList(criteria, pageNo, pageSize);
    }

    @Override
    public List<History> find(long person_id, long product_id) {
        DetachedCriteria criteria = DetachedCriteria.forClass(History.class)
                .add(Restrictions.eq("person_id", person_id))
                .add(Restrictions.eq("product_id", product_id));
        return historyDAO.list(criteria).size() > 0 ? historyDAO.list(criteria) : null;
    }

}
