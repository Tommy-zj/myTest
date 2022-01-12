package com.bbkj.common.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bbkj.action.ChatListAction;
import com.bbkj.domain.ChatList;
import com.bbkj.domain.Member;
import com.bbkj.domain.Demand;
import com.bbkj.domain.Message;
import com.bbkj.service.ChatListService;
import com.bbkj.service.DemandService;
import com.bbkj.service.MemberService;
import com.bbkj.socket.WebSocketUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONString;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import javax.websocket.Session;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO 需求被接单后通知发需求的人，需要付款，付款后方能让技工前往提供技术支持
 * @date 2020/10/30 17:04
 */
@Slf4j
public class NoticeUtils {
    private static ChatListService chatListService = SpringUtil.getBean(ChatListService.class);
    private static MemberService memberService = SpringUtil.getBean(MemberService.class);
    private static DemandService demandService = SpringUtil.getBean(DemandService.class);
    //全部连接数
    public static Map<String, Session> counts = WebSocketUtils.counts;
    private static Demand request;
    public static String person_id;


    public static void notice(Object message, long other_id, long create_id) throws IOException {
        try {
            String img = "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3490593561,3100696880&fm=26&gp=0.jpg";
            //系统通过websocket通知对方付款。无论如何，先将通知消息保存到文件
            WebSocketUtils.append(other_id + "to" + "0", message);
            //创建聊天列表
            log.info(chatListService.toString());
            Boolean IsCreate = ChatUtils.addChat(other_id, 0, chatListService);
            if (IsCreate == true) {
                //需要创建一个新的聊天列表；
                ChatList chatList = ChatListAction.CreateEntity(img, "系统通知", other_id, 0);
                chatListService.save(chatList);
                log.info("新的系统通知聊天列表添加完成");
            }
            boolean SysISCreate = ChatUtils.addChat(0, other_id, chatListService);
            if (SysISCreate == true) {
                //后台需要创建一个新的聊天列表；
                //从member表找出对应的用户信息
                DetachedCriteria criteria = DetachedCriteria.forClass(Member.class);
                criteria.add(Restrictions.eq("id", create_id));
                List<Member> members = memberService.list(criteria);
                if (members.size() > 0) {
                    Member member = members.get(0);
                    ChatList chatList = ChatListAction.CreateEntity(member.getAvatar(), member.getName(), 0, other_id);
                    chatListService.save(chatList);
                }
            }
            log.info("查看全部在线的用户");
            log.info(counts.toString());
            log.info("对面的key");
            log.info(person_id);
            log.info(String.valueOf(counts.containsKey(person_id)));
            if (counts.containsKey(person_id)) {
                log.info("发需求人在线");
                if (message != null && !message.equals(null)) {
                    String jsonObject = JSON.toJSONString(message);
                    counts.get(person_id).getAsyncRemote().sendText(jsonObject);
                    return;
                }
                counts.get(person_id).getAsyncRemote().sendText("news");
            } else {
                log.info("发需求的人不在线");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @description: 是否被接单
     * @param: * @param request_id
     * @return: boolean
     * @author JJ
     * @date: 2020/11/3 9:39
     */
    public static boolean isOrder(long request_id) {
        request = demandService.findById(request_id);
        if (request.getStatus() == 1l) {
            //未被接单，具备被接单的条件
            return false;
        } else {
            //不具备被接单的条件
            return true;
        }
    }

    /**
     * @description: 设置需求的状态id=3的已接单状态
     * @param: * @param
     * @return: void
     * @author JJ
     * @date: 2020/11/3 9:52
     */
    public static void modifyState() {
        //设置需求的状态为已接单
        request.setStatus(3);
        demandService.update(request);
    }

    public static String createOrderNumber() {
        int r1 = (int) (Math.random() * (10));//产生2个0-9的随机数
        int r2 = (int) (Math.random() * (10));
        long now = System.currentTimeMillis();//一个13位的时间戳
        String paymentID = String.valueOf(r1) + String.valueOf(r2) + String.valueOf(now);// 订单ID
        return paymentID;
    }


}
