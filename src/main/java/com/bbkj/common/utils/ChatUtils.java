package com.bbkj.common.utils;

import com.bbkj.common.BaseAction;
import com.bbkj.domain.ChatList;
import com.bbkj.domain.Member;
import com.bbkj.domain.User;
import com.bbkj.service.ChatListService;
import com.bbkj.service.MemberService;
import com.bbkj.service.UserService;
import com.bbkj.socket.WebSocketUtils;
import javassist.NotFoundException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.context.ContextLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * @author JJ
 * @version 1.0
 * @description: TODO
 * @date 2020/9/23 17:34
 */
@Setter
@Getter
@Slf4j
public class ChatUtils extends BaseAction {
    private ChatListService chatListService = (ChatListService) ContextLoader.getCurrentWebApplicationContext().getBean("chatListService");
    private MemberService memberService = (MemberService) ContextLoader.getCurrentWebApplicationContext().getBean("mechanicService");

    /**
     * @description: 获取指定文件夹下的全部文件名
     * @param: * @param path 文件名
     * @return: java.util.List<java.lang.String>
     * @author JJ
     * @date: 2020/9/23 17:51
     */
    public static List<String> getAllFileName(String path) {
        List<String> fileNameList = new ArrayList<>();
        File file = new File(path);
        File[] tempList = file.listFiles();
        for (File file1 : tempList) {
            if (file1.isFile()) {
                fileNameList.add(file1.getName());
            }
            if (file1.isDirectory()) {
                getAllFileName(file1.getAbsolutePath());
            }
        }
        return fileNameList;
    }

    /**
     * @description: 按id找到和该id有聊天记录的文件
     * @param: * @param person_id
     * @return: java.util.List
     * @author JJ
     * @date: 2020/9/24 9:22
     */
    public static List findById(long person_id) {
        List<String> data = getAllFileName(WebSocketUtils.rootUrl);
        List fileName = new ArrayList();
        for (String name : data) {
            String[] names = name.split("\\.");
            String[] left = names[0].split("to");
            for (String id : left) {
                if (person_id == Long.parseLong(id)) {
                    fileName.add(name);
                } else {
                    break;
                }
            }
        }
        return fileName;
    }

    public static ChatList jsonStrToJavas(String chatLists) {
        JSONObject jsonObject = JSONObject.fromObject(chatLists);
        ChatList chatList = (ChatList) JSONObject.toBean(jsonObject, ChatList.class);
        return chatList;
    }

    /**
     * @param other_id
     * @description: 检索数据库，没有次记录则添加
     * @param: * @param person_id
     * @return: boolean true,实体不存在，需要添加，false，实体存在，不需要添加
     * @author JJ
     * @date: 2020/9/24 20:46
     */
    public static boolean addChat(long person_id, long other_id, ChatListService chatListService) {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(ChatList.class);
            criteria.add(Restrictions.eq("person_id", person_id));
            criteria.add(Restrictions.eq("other_id", other_id));
            Number number = chatListService.count(criteria);
            if (number.equals(0)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    /**
     * @param other_id
     * @param chatListService
     * @description: 找到对应的聊天列表，并同步时间戳
     * @param: * @param person_id
     * @return: boolean
     * @author JJ
     * @date: 2020/9/25 17:18
     */
    public static boolean updates(long person_id, long other_id, ChatListService chatListService) {
        DetachedCriteria criteria = DetachedCriteria.forClass(ChatList.class);
        criteria.add(Restrictions.eq("person_id", person_id));
        criteria.add(Restrictions.eq("other_id", other_id));
        List chatList = chatListService.list(criteria);
        if (chatList.size() == 0) {
            return false;
        }
        ChatList chatList1 = (ChatList) chatList.get(0);
        chatList1.setLast_modify_time(System.currentTimeMillis());
        chatListService.update(chatList1);
        return true;
    }

    public static boolean adding(ChatList chatList, ChatListService chatListService) {
        chatListService.save(chatList);
        return true;
    }

    /**
     * @param other_id
     * @description: 找到Mechanic对象组成ChatList对象
     * @param: * @param person_id
     * @return: com.beped.chat.domain.ChatList
     * @author JJ
     * @date: 2020/9/24 20:45
     */
    public static ChatList createChatList(long person_id, long other_id, MemberService memberService, UserService userService) throws NotFoundException {
        ChatList chatList = new ChatList();
        try {
            Member member = memberService.findById(other_id);
            chatList.setImage_url(member.getAvatar());
            chatList.setName(member.getName());
        } catch (Exception e) {
            log.info("报什么错");
            log.info(e.getMessage());
            e.printStackTrace();
            User user = userService.findAdressById(other_id);
            chatList.setImage_url(user.getAvatar());
            chatList.setName(user.getName());
        }
        chatList.setInfo_num(0);
        chatList.setPerson_id(person_id);
        chatList.setOther_id(other_id);
        if (person_id > other_id) {
            chatList.setFile_name(person_id + "to" + other_id);
        } else {
            chatList.setFile_name(other_id + "to" + person_id);
        }
        return chatList;
    }
}
