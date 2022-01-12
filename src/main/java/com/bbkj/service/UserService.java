package com.bbkj.service;

import com.bbkj.common.utils.Page;
import com.bbkj.domain.User;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.List;

public interface UserService {

    public List<User> list(DetachedCriteria criteria);

    public User findAdressById(Long id);

    public Serializable save(User user);

    public void update(User user);

    public void remove(User user);

    public void remove(long[] ids);

    public List<User> listAll();


    public Page pageList(DetachedCriteria criteria,
                         final int pageNo, final int pageSize);

    public Number findByNames(String name);

    List<User> findByName(String name);

    Page list(int pageNo, int pageSize);

    Boolean admin(long personId);
}
