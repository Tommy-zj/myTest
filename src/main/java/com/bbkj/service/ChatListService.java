package com.bbkj.service;


import com.bbkj.common.utils.Page;
import com.bbkj.domain.ChatList;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.List;

public interface ChatListService {

    public ChatList findById(Long id);

    public Serializable save(ChatList chatList);

    public void update(ChatList chatList);

    public void remove(ChatList chatList);

    public void remove(long[] ids);

    public List<ChatList> listAll();

    public List<ChatList> list(DetachedCriteria criteria);

    public Page pageList(DetachedCriteria criteria,
                         final int pageNo, final int pageSize);

    public Number count(DetachedCriteria criteria);

    public List<ChatList> findByPersonId(long person_id);

    ChatList findByPersonIdOtherId(long person_id, long other_id);
}
