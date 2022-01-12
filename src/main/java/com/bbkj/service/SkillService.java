package com.bbkj.service;

import com.bbkj.common.utils.Page;
import com.bbkj.domain.Skill;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.List;

public interface SkillService {
    public Skill findById(Long id);

    public Serializable save(Skill member);

    public void update(Skill member);

    public void remove(Skill member);

    public void remove(long[] ids);

    public List<Skill> listAll();

    public List<Skill> list(DetachedCriteria criteria);

    public Page pageList(DetachedCriteria criteria,
                         final int pageNo, final int pageSize);
}
