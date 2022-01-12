package com.bbkj.service.impl;


import com.bbkj.common.utils.Page;
import com.bbkj.dao.CarouselDao;
import com.bbkj.domain.Carousel;
import com.bbkj.service.CarouselService;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service(value = "carouselService")
public class CarouselServiceImpl implements CarouselService {
    @Autowired
    private CarouselDao carouselDAO;

    @Override
    public Carousel findById(Long id) {
        return carouselDAO.load(id);
    }

    @Override
    public Serializable save(Carousel carousel) {
        return carouselDAO.save(carousel);
    }

    @Override
    public void update(Carousel carousel) {

    }

    @Override
    public void remove(Carousel carousel) {
        carouselDAO.remove(carousel);
    }

    @Override
    public void remove(long[] ids) {
        carouselDAO.remove(ids);
    }

    @Override
    public List<Carousel> listAll() {
        return carouselDAO.listAll();
    }

    @Override
    public List<Carousel> list(DetachedCriteria criteria) {
        return carouselDAO.list(criteria);
    }

    @Override
    public Page pageList(DetachedCriteria criteria, int pageNo, int pageSize) {
        return carouselDAO.pageList(criteria, pageNo, pageSize);
    }
}
