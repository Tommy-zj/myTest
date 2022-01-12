package com.bbkj.service.impl;


import com.bbkj.common.utils.Page;
import com.bbkj.dao.ReturnImgDao;
import com.bbkj.domain.ReturnImg;
import com.bbkj.service.ReturnImgService;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO
 * @date 2020/11/16 9:58
 */
@Service
public class ReturnImgServiceImpl implements ReturnImgService {
    @Autowired
    private ReturnImgDao returnImgDAO;


    @Override
    public ReturnImg findAdressById(Long id) {
        return returnImgDAO.load(id);
    }

    @Override
    public Serializable save(ReturnImg collect) {
        return returnImgDAO.save(collect);
    }

    @Override
    public void update(ReturnImg collect) {
        returnImgDAO.update(collect);
    }

    @Override
    public void remove(ReturnImg collect) {
        returnImgDAO.remove(collect);
    }

    @Override
    public void remove(long[] ids) {
        returnImgDAO.remove(ids);
    }

    @Override
    public List<ReturnImg> listAll() {
        return returnImgDAO.listAll();
    }

    @Override
    public List<ReturnImg> list(DetachedCriteria criteria) {
        return returnImgDAO.list(criteria);
    }

    @Override
    public Page pageList(DetachedCriteria criteria, int pageNo, int pageSize) {
        return returnImgDAO.pageList(criteria, pageNo, pageSize);
    }
}
