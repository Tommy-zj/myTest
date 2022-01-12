package com.bbkj.service.impl;

import com.bbkj.common.utils.Page;
import com.bbkj.dao.DemandImgDao;
import com.bbkj.domain.DemandImg;
import com.bbkj.service.DemandImgService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class DemandImgServiceImpl implements DemandImgService {
    @Autowired
    private DemandImgDao demandImgDAO;

    @Override
    public DemandImg findById(Long id) {
        return demandImgDAO.load(id);
    }

    @Override
    public Serializable save(DemandImg demandImg) {
        return demandImgDAO.save(demandImg);
    }

    @Override
    public void update(DemandImg demandImg) {
        demandImgDAO.update(demandImg);
    }

    @Override
    public void remove(DemandImg demandImg) {
        demandImgDAO.remove(demandImg);
    }

    @Override
    public void remove(long[] ids) {
        demandImgDAO.remove(ids);
    }

    @Override
    public List<DemandImg> listAll() {
        return demandImgDAO.listAll();
    }

    @Override
    public List<DemandImg> list(DetachedCriteria criteria) {
        return demandImgDAO.list(criteria);
    }

    @Override
    public Page pageList(DetachedCriteria criteria, int pageNo, int pageSize) {
        return demandImgDAO.pageList(criteria, pageNo, pageSize);
    }

    @Override
    public List<DemandImg> findByRequestId(long RequestId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(DemandImg.class);
        criteria.add(Property.forName("demand_id").eq(RequestId));
        return demandImgDAO.list(criteria);
    }
    @Override
    public List<DemandImg> findByDemandId(long demand_id) {
        DetachedCriteria criteria2 = DetachedCriteria.forClass(DemandImg.class);
        criteria2.add(Restrictions.eq("demand_id", demand_id));
        List<DemandImg> demandImgs = demandImgDAO.list(criteria2);
        return demandImgs.size() > 0 ? demandImgs : null;
    }


}
