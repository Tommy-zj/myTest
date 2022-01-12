package com.bbkj.service;


import com.bbkj.common.utils.Page;
import com.bbkj.domain.Image;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.List;

public interface ImgService {
    public Image findById(Long id);

    public Serializable save(Image image);

    public void update(Image image);

    public void remove(Image image);

    public void remove(long[] ids);

    public List<Image> listAll();

    public List<Image> list(DetachedCriteria criteria);

    public Page pageList(DetachedCriteria criteria,
                         final int pageNo, final int pageSize);

    public List<Image> findByProductId(long product_id);

}
