package com.bbkj.socket;

import com.bbkj.common.utils.ChatUtils;
import com.bbkj.domain.ChatList;
import com.bbkj.service.ChatListService;
import com.bbkj.service.MemberService;
import com.bbkj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO 聊天室
 * @date 2020/9/21 9:22
 * @date 2020/9/21 9:22
 */
//other_id对方的id，person_id自己的id，room两人id组成的房间名称。
//@Controller
@Component
@Slf4j
@ServerEndpoint("/websocket/{token}/{other_id}/{person_id}/{room}")
public class WebSocketServer {
    private static ChatListService chatListService;
    private static MemberService memberService;
    private static UserService userService;
    //全部房间
    public static Map<String, Set<Session>> rooms = WebSocketUtils.rooms;
    //全部连接数
    public static Map<String, Session> counts = WebSocketUtils.counts;

    @Autowired
    public void setChatListService(ChatListService chatListService) {
        WebSocketServer.chatListService = chatListService;
    }

    @Autowired
    public void setMemberService(MemberService memberService) {
        WebSocketServer.memberService = memberService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        WebSocketServer.userService = userService;
    }


    /**
     * @param session
     * @description: 创建一个新的连接
     * @param: * @param room
     * @return: void
     * @author JJ
     * @date: 2020/9/18 15:45
     */
    @OnOpen
    public void onOpen(@PathParam("room") String room, @PathParam("person_id") String person_id, @PathParam("token") String token,
                       Session session) throws Exception {
        log.info("查看token的值" + token);
        if (token.length() > 0) {
            try {
                //以one为房间号，说明只是初始化的主连接
                if (room.equals("one")) {
                    log.info("连接成功");
                    log.info("这是小程序初始化的时候的连接");
                    // 全部用户，以person_id为键
                    counts.put(person_id, session);
                    log.info("总人数" + counts.size());
                    return;
                }
                //加入房间
                WebSocketUtils.CreateOrJoin(room, session);
                List<String> messages = WebSocketUtils.getMessage(room, 1, 15);
                //将用户添加进相应的房间，或创建一个新的房间,返回n条聊天信息。
                if (messages == null) {
                    log.info("没有对应的聊天信息");
                    return;
                }
                JSONObject jsonObject = WebSocketUtils.packInfo(messages);
                session.getAsyncRemote().sendText(jsonObject.toString());
                log.info("[WebSocketServer] Connected : room = " + room + "房间人数:" + rooms.get(room).size());
                log.info("进入小程序聊天房间成功!");
            } catch (Exception e) {
                session.getAsyncRemote().sendText(e.getMessage());
            }
            return;
        }
        log.info("不是本系统授权的用户");
        this.onClose(room, session);
    }

    /**
     * @param info
     * @param session
     * @description: 收到客户端的信息
     * @param: * @param room
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/9/18 15:45
     */
    @OnMessage
    public String onMessage(@PathParam("room") String room, @PathParam("person_id") String person_id, @PathParam("other_id") String other_id,
                            String info, Session session) throws Exception {
        log.info("info的值" + info);
        Message message = WebSocketUtils.jsonStrToJavas(info);
        if (message.getMsg().equals("&")) {
            //心跳包保持存活
            log.info("心跳包检查成功！");
            return "&";
        }
        log.info("正常的聊天信息" + info);
        //正常的聊天内容
        long otherId = Long.parseLong(other_id);
        long personId = Long.parseLong(person_id);
        //先添加新的聊天列表
        if (ChatUtils.addChat(personId, otherId, chatListService) == true) {
            log.info("准备添加新消息");
            ChatList chatList = ChatUtils.createChatList(personId, otherId, memberService, userService);
            log.info("新消息添加完成");
            //为true，需要添加新的聊天列表
            if (ChatUtils.adding(chatList, chatListService) == true) {
                log.info("本人聊天列表添加成功");
            }
        } else {
            log.info("本人聊天列表已存在");
            if (ChatUtils.updates(personId, otherId, chatListService) == true) {
                log.info("用户时间戳同步成功！");
            } else {
                log.info("用户同步时间戳失败");
            }
        }
        if (ChatUtils.addChat(otherId, personId, chatListService) == true) {
            ChatList chatList = ChatUtils.createChatList(otherId, personId, memberService, userService);
            //为true，需要添加新的聊天列表
            if (ChatUtils.adding(chatList, chatListService) == true) {
                log.info("对方聊天列表添加成功");
            }
        } else {
            log.info("对方聊天列表已存在");
        }
        //如果房间人数大于1，说明两人都在聊天室
        //消息持久化
        WebSocketUtils.append(room, message);
        //将消息变成JsonObject格式
        JSONObject jsonObject = WebSocketUtils.StringToJsonObject(info);
        if (rooms.get(room).size() > 1) {
            log.info("两个人在聊天室");
            //消息发送的同时同步聊天列表的时间戳
            if (ChatUtils.updates(otherId, personId, chatListService) == true) {
                log.info("对方时间戳同步成功！");
            } else {
                log.info("对方同步时间戳失败");
            }
            //两人都在聊天室,推送消息;
            WebSocketUtils.broadcast(room, jsonObject.toString());
        } else {
            log.info("一个人在聊天室");
            //只有一个人在聊天室,推送消息给对方。
            // counts.get(other_id).getAsyncRemote().sendText(WebSocketUtils.convertObject(WebSocketUtils.newMessage));
            if (counts.containsKey(other_id)) {
                log.info("对方在线");
                log.info(counts.toString());
                try {
                    counts.get(other_id).getAsyncRemote().sendText("news");
                } catch (Exception e) {
                    counts.remove(other_id);
                    log.info("捕捉了对方不在线的异常！");
                }

            } else {
                log.info("对方不在线");
                log.info(counts.toString());
            }
            //还得发送新信息给自己，保证消息是持久化后才推送的，保证消息的同步
            session.getAsyncRemote().sendText(jsonObject.toString());
        }
        return null;
    }

    /**
     * @param throwable
     * @param session
     * @description: 出错处理
     * @param: * @param room
     * @return: void
     * @author JJ
     * @date: 2020/9/18 15:45
     */
    @OnError
    public void onError(@PathParam("room") String room,
                        Throwable throwable,
                        Session session) {
        log.info("[WebSocketServer] Connection Exception : room = " + room + " , throwable = " + throwable.getMessage());
        WebSocketUtils.remove(room);
    }

    /**
     * @param session
     * @description: 断开链接
     * @param: * @param room
     * @return: void
     * @author JJ
     * @date: 2020/9/18 15:46
     */
    @OnClose
    public void onClose(@PathParam("room") String room,
                        Session session) throws Exception {
        log.info(room + "断开连接");
        try {
            rooms.get(room).remove(session);
        } catch (Exception e) {
            log.info("连接被断开，原因" + e.getMessage());
            WebSocketUtils.remove(room);
        }
        log.info("[WebSocketServer] 连接被关闭 : 房间号 =" + room);
    }
}
