package com.bbkj.dao.impl;

import com.bbkj.common.genericDao.hibernate.GenericDaoImpl;
import com.bbkj.dao.CarouselDao;
import com.bbkj.domain.Carousel;
import org.springframework.stereotype.Repository;

@Repository
public class CarouselDaoImpl extends GenericDaoImpl<Carousel> implements CarouselDao {
}
