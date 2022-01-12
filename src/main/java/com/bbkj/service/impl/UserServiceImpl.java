package com.bbkj.service.impl;


import com.bbkj.common.utils.Page;
import com.bbkj.dao.UserDao;
import com.bbkj.domain.User;
import com.bbkj.service.UserService;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDAO;


    public void setUserDAO(UserDao userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public List<User> list(DetachedCriteria criteria) {
        return userDAO.list(criteria);
    }

    @Override
    public User findAdressById(Long id) {
        User user = userDAO.load(id);
        Hibernate.initialize(user);
        return user;
    }

    @Override
    public Serializable save(User user) {
        return userDAO.save(user);
    }

    @Override
    public void update(User user) {
        userDAO.update(user);
    }

    @Override
    public void remove(User user) {
        userDAO.remove(user);
    }

    @Override
    public void remove(long[] ids) {
        userDAO.remove(ids);
    }

    @Override
    public List<User> listAll() {
        return userDAO.listAll();
    }

    @Override
    public Page pageList(DetachedCriteria criteria, int pageNo, int pageSize) {
        return userDAO.pageList(criteria, pageNo, pageSize);
    }

    @Override
    public Number findByNames(String name) {
        DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
        criteria.add(Restrictions.eq("name", name));
        return userDAO.count(criteria);
    }

    @Override
    public List<User> findByName(String name) {
        DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
        criteria.add(Restrictions.eq("name", name));
        return userDAO.list(criteria);
    }

    @Override
    public Page list(int pageNo, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
        Page page = userDAO.pageList(criteria, pageNo, pageSize);
        return page;
    }

    @Override
    public Boolean admin(long personId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(User.class).
                add(Restrictions.eq("id", personId));
        return userDAO.list(criteria).size() > 0 ? true : false;
    }
}
