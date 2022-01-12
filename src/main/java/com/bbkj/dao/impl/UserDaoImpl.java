package com.bbkj.dao.impl;

import com.bbkj.common.genericDao.hibernate.GenericDaoImpl;
import com.bbkj.dao.UserDao;
import com.bbkj.domain.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl extends GenericDaoImpl<User> implements UserDao {

}
