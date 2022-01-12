package com.bbkj.service.impl;


import com.bbkj.common.utils.Page;
import com.bbkj.dao.SkillDao;
import com.bbkj.domain.Skill;
import com.bbkj.service.SkillService;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO
 * @date 2021/2/26 9:43
 */
@Service
public class SkillServiceImpl implements SkillService {
    @Autowired
    private SkillDao skillDAO;

    @Override
    public Skill findById(Long id) {
        return skillDAO.load(id);
    }

    @Override
    public Serializable save(Skill member) {
        return skillDAO.save(member);
    }

    @Override
    public void update(Skill member) {
        skillDAO.update(member);
    }

    @Override
    public void remove(Skill member) {
        skillDAO.remove(member);
    }

    @Override
    public void remove(long[] ids) {
        skillDAO.remove(ids);
    }

    @Override
    public List<Skill> listAll() {
        return skillDAO.listAll();
    }

    @Override
    public List<Skill> list(DetachedCriteria criteria) {
        return skillDAO.list(criteria);
    }

    @Override
    public Page pageList(DetachedCriteria criteria, int pageNo, int pageSize) {
        return skillDAO.pageList(criteria, pageNo, pageSize);
    }


}
