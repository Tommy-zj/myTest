package com.bbkj.service.impl;


import com.bbkj.common.utils.Page;
import com.bbkj.dao.ChatListDao;
import com.bbkj.domain.ChatList;
import com.bbkj.service.ChatListService;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO
 * @date 2020/9/21 16:14
 */
@Setter
@Getter
@Service
public class ChatListServiceImpl implements ChatListService {
    @Autowired
    private ChatListDao chatListDAO;

    @Override
    public ChatList findById(Long id) {
        return chatListDAO.load(id);
    }

    @Override
    public Serializable save(ChatList chatList) {
        return chatListDAO.save(chatList);
    }

    @Override
    public void update(ChatList chatList) {
        chatListDAO.update(chatList);
    }

    @Override
    public void remove(ChatList chatList) {
        chatListDAO.remove(chatList);
    }

    @Override
    public void remove(long[] ids) {
        chatListDAO.remove(ids);
    }

    @Override
    public List<ChatList> listAll() {
        return chatListDAO.listAll();
    }

    @Override
    public List<ChatList> list(DetachedCriteria criteria) {
        return chatListDAO.list(criteria);
    }

    @Override
    public Page pageList(DetachedCriteria criteria, int pageNo, int pageSize) {
        return chatListDAO.pageList(criteria, pageNo, pageSize);
    }

    @Override
    public Number count(DetachedCriteria criteria) {
        chatListDAO.list(criteria);
        return chatListDAO.list(criteria).size();
    }

    @Override
    public List<ChatList> findByPersonId(long person_id) {
        DetachedCriteria criteria = DetachedCriteria.forClass(ChatList.class);
        criteria.add(Restrictions.eq("person_id", person_id));
        criteria.addOrder(Order.desc("last_modify_time"));  //按时间戳倒序排列聊天列表
        return chatListDAO.list(criteria);
    }

    @Override
    public ChatList findByPersonIdOtherId(long person_id, long other_id) {
        DetachedCriteria criteria = DetachedCriteria.forClass(ChatList.class);
        criteria.add(Restrictions.eq("person_id", person_id));
        criteria.add(Restrictions.eq("other_id", other_id));
        List<ChatList> chatLists = chatListDAO.list(criteria);
        return chatLists.size() > 0 ? chatLists.get(0) : null;
    }
}
