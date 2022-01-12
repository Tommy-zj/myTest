package com.bbkj.dao.impl;


import com.bbkj.common.genericDao.hibernate.GenericDaoImpl;
import com.bbkj.dao.ChatListDao;
import com.bbkj.domain.ChatList;
import org.springframework.stereotype.Repository;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO
 * @date 2020/9/21 16:11
 */
@Repository
public class ChatListDaoImpl extends GenericDaoImpl<ChatList> implements ChatListDao {
}
