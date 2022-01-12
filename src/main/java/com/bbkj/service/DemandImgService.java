package com.bbkj.service;

import com.bbkj.common.utils.Page;
import com.bbkj.domain.DemandImg;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.List;

public interface DemandImgService {
    DemandImg findById(Long id);

    Serializable save(DemandImg demandImg);

    void update(DemandImg demandImg);

    void remove(DemandImg demandImg);

    void remove(long[] ids);

    List<DemandImg> listAll();

    List<DemandImg> list(DetachedCriteria criteria);

    Page pageList(DetachedCriteria criteria,
                  final int pageNo, final int pageSize);

    List<DemandImg> findByRequestId(long RequestId);

    List<DemandImg> findByDemandId(long demand_id);
}
