package com.bbkj.service.impl;


import com.bbkj.common.utils.Page;
import com.bbkj.dao.ImgDao;
import com.bbkj.domain.Image;
import com.bbkj.service.ImgService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class ImgServiceImpl implements ImgService {
    @Autowired
    private ImgDao imgDAO;

    @Override
    public Image findById(Long id) {
        return imgDAO.load(id);
    }

    @Override
    public Serializable save(Image image) {
        return imgDAO.save(image);
    }

    @Override
    public void update(Image image) {
        imgDAO.update(image);

    }

    @Override
    public void remove(Image image) {
        imgDAO.remove(image);

    }

    @Override
    public void remove(long[] ids) {
        imgDAO.remove(ids);

    }

    @Override
    public List<Image> listAll() {
        return imgDAO.listAll();
    }

    @Override
    public List<Image> list(DetachedCriteria criteria) {
        return imgDAO.list(criteria);
    }

    @Override
    public Page pageList(DetachedCriteria criteria, int pageNo, int pageSize) {
        return imgDAO.pageList(criteria, pageNo, pageSize);
    }

    @Override
    public List<Image> findByProductId(long product_id) {
        DetachedCriteria criteria1 = DetachedCriteria.forClass(Image.class)
                .add(Restrictions.eq("product_id", product_id));
        List<Image> images = imgDAO.list(criteria1);
        return images.size() > 0 ? images : null;
    }

}
