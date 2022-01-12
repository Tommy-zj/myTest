package com.bbkj.service;

import com.bbkj.common.utils.Page;
import com.bbkj.domain.Carousel;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.List;

public interface CarouselService {
    public Carousel findById(Long id);

    public Serializable save(Carousel carousel);

    public void update(Carousel carousel);

    public void remove(Carousel carousel);

    public void remove(long[] ids);

    public List<Carousel> listAll();

    public List<Carousel> list(DetachedCriteria criteria);

    public Page pageList(DetachedCriteria criteria,
                         final int pageNo, final int pageSize);
}
