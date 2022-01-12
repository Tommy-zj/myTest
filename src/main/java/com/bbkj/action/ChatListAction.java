package com.bbkj.action;


import com.bbkj.common.BaseAction;
import com.bbkj.common.utils.ChatUtils;
import com.bbkj.domain.ChatList;
import com.bbkj.service.ChatListService;
import com.bbkj.socket.WebSocketUtils;
import com.bbkj.socket.WebsocketUtilsBat;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.List;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO
 * @date 2020/9/21 16:31
 */
@Setter
@Getter
@Controller
@ParentPackage("post")
@Namespace("/chat")
@Slf4j
public class ChatListAction extends BaseAction {
    @Autowired
    private ChatListService chatListService;
    private long person_id;
    private long other_id;
    private String name;
    private String img;
    private String fileName;
    private long id;
    private int pageNo;
    private int pageSize;
    private ChatList chatList = new ChatList();

    public static ChatList CreateEntity(String img, String name, long person_id, long other_id) {
        ChatList chatList = new ChatList();
        chatList.setImage_url(img);
        chatList.setName(name);
        chatList.setPerson_id(person_id);
        chatList.setOther_id(other_id);
        if (other_id > person_id) {
            chatList.setFile_name(other_id + "to" + person_id);
        } else {
            chatList.setFile_name(person_id + "to" + other_id);
        }
        return chatList;
    }

    /**
     * @description: 先查看表中有无此相同的person_id和other_id, 有则不添加，反之添加
     * @param: * @param
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/9/24 9:51
     */
    @Action(value = "add", interceptorRefs = {@InterceptorRef(value = "json"),
            @InterceptorRef(value = "checkStack")})
    public String add() throws IOException {
        try {
            if (ChatUtils.addChat(person_id, other_id, chatListService) == true) {
                ChatUtils.adding(CreateEntity(img, name, person_id, other_id), chatListService);
                notifySuccess("本人的聊天列表添加成功！");
            } else {
                notifyError("本人的聊天列表已存在！");
            }
            if (ChatUtils.addChat(other_id, person_id, chatListService) == true) {
                ChatUtils.adding(CreateEntity(img, name, other_id, person_id), chatListService);
                notifySuccess("对方的聊天列表添加成功！");
                return null;
            }
            notifyError("对方的聊天列表已存在！");
        } catch (Exception e) {
            response(500, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Action(value = "delete")
    public String delete() throws Exception {
        ChatList chatList = chatListService.findById(id);
        chatListService.remove(chatList);
        notifySuccess("删除成功！");
        return null;
    }

    @Action(value = "list")
    public String listAll() throws Exception {
        try {
            List<ChatList> data = chatListService.findByPersonId(person_id);
            for (ChatList chatList : data) {
                //按照文件名字找到对应的文件内容
                List<String> allInfo = WebsocketUtilsBat.getMessage(chatList.getFile_name(), pageNo, 15);
                if (allInfo != null) {
                    ChatList chatLists = WebSocketUtils.isNewInfo(chatList, pageNo, allInfo);
                    //获取到每个聊天列表的最新消息，第一条
                    List<String> newChat = WebSocketUtils.getMessage(chatLists.getFile_name(), 1, 1);
                    //为每个消息列表设置一个新消息
                    WebSocketUtils.setChat_record(newChat, chatLists);
                }
            }
            returnData(200, "success", data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Action(value = "edit")
    public String edit() throws IOException {
        try {
            ChatList chatList = chatListService.findByPersonIdOtherId(person_id, other_id);
            if (chatList != null) {
                chatList.setLast_modify_time(System.currentTimeMillis());
                chatListService.update(chatList);
                notifySuccess("update success");
                return null;
            }
            log.info("聊天列表不存在");
            notifyError("聊天列表不存在");
        } catch (Exception e) {
            e.printStackTrace();
            response(500, e.getMessage());
        }
        return null;
    }

    @Action(value = "gainInfo")
    public String gainInfo() throws IOException {
        //获取到聊天信息，添加进分页器
        try {
            List<String> infos = WebsocketUtilsBat.getMessage(fileName, pageNo, pageSize);
            JSONObject jsonObject = WebSocketUtils.packInfo(infos);
            // writeJson(jsonObject);
             returnData(200, "true", jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            response(500, e.getMessage());
        }
        return null;
    }
}
