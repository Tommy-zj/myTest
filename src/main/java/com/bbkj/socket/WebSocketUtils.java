package com.bbkj.socket;

import com.bbkj.common.SystemGlobal;
import com.bbkj.domain.ChatList;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

import javax.websocket.Session;
import java.io.*;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO 聊天室工具类
 * @date 2020/9/21 9:25
 */
@Slf4j
public class WebSocketUtils {
    //总房间
    public static Map<String, Set<Session>> rooms = new ConcurrentHashMap<>();
    //全部人
    public static Map<String, Session> counts = new ConcurrentHashMap<>();

    //项目聊天记录的文件夹
    public static String rootUrl = SystemGlobal.chatUrl;

    /**
     * @param session
     * @description: 创建房间或将加入房间, 房间名为minIdTomaxId
     * @param: * @param room 房间名
     * @return: java.util.Map<java.lang.String, java.util.Set < javax.websocket.Session>>
     * @author JJ
     * @date: 2020/9/18 14:53
     */
    public static void CreateOrJoin(String room, Session session) {
        if (!rooms.containsKey(room)) {
            //房间不存在时，创建房间
            Set<Session> room1 = new HashSet<>();
            //添加用户
            room1.add(session);
            rooms.put(room, room1);
            return;
        }
        //房间已存在，直接添加用户到相应的房间
        rooms.get(room).add(session);
        log.info("房间数 = " + rooms.size());
    }


    public static void broadcast(String room, String message) throws Exception {
        if (rooms.get(room) == null) {
            log.info("房间不存在");
        } else {
            for (Session sessions : rooms.get(room)) {
                sessions.getBasicRemote().sendText(message);
            }
        }
    }

    /*
    Remove Session
     */
    public static void remove(String room) {
        try {
            rooms.remove(room);
            log.info("房间数 = " + rooms.size());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * @description: json字符串转message对象
     * @param: * @param message
     * @return: com.beped.socket.Info
     * @author JJ
     * @date: 2020/9/19 9:40
     */
    public static Message jsonStrToJavas(String message) {
        JSONObject jsonObject = JSONObject.fromObject(message);
        Message info1 = (Message) JSONObject.toBean(jsonObject, Message.class);
        return info1;
    }

    /**
     * @description: java对象转json字符串
     * @param: * @param info
     * @return: java.lang.String
     * @author JJ
     * @date: 2020/9/19 9:40
     */
    public static String convertObject(Object info) {
        JSONObject jsonObject = JSONObject.fromObject(info);
        return jsonObject.toString();
    }

    /**
     * @param pageSize 每次获取的信息条数
     * @param pageNo   获取信息的页数
     * @description: 获取对应房间的len条聊天信息
     * @param: * @param room 房间名称
     * @return: java.util.List<java.lang.String>
     * @author JJ
     * @date: 2020/9/21 11:29
     */
    public static List<String> getMessage(String room, int pageNo, int pageSize) throws IOException {
        File file = new File(rootUrl + room + ".txt");
        log.info("文件路径");
        log.info(rootUrl + room + ".txt");
        if (!file.exists()) {
            log.info("文件不存在");
            return null;
        }
        log.info("文件存在");
        List<String> data = WebSocketUtils.readForPage(file, pageNo, pageSize);
        return data;
    }


    /**
     * @param message
     * @description: 每次添加都在第一行
     * @param: * @param filename
     * @return: void
     * @author JJ
     * @date: 2020/9/27 17:34
     */
    public static void append(String filename, Object message) throws IOException {//pos是插入的位置
        filename = rootUrl + filename + ".txt";
        String insertContent = convertObject(message) + "\n";
        int pos = 0;
        File tmp = File.createTempFile("tmp", null);
        tmp.deleteOnExit();
        try {
            RandomAccessFile raf = new RandomAccessFile(filename, "rw");
            FileOutputStream tmpOut = new FileOutputStream(tmp);
            FileInputStream tmpIn = new FileInputStream(tmp);
            raf.seek(pos);//首先的话是0
            byte[] buf = new byte[64];
            int hasRead = 0;
            while ((hasRead = raf.read(buf)) > 0) {
                //把原有内容读入临时文件
                tmpOut.write(buf, 0, hasRead);
            }
            raf.seek(pos);
            raf.write(insertContent.getBytes());
            //追加临时文件的内容
            while ((hasRead = tmpIn.read(buf)) > 0) {
                raf.write(buf, 0, hasRead);
            }
        } catch (Exception e) {
            log.info("错误" + e.getMessage());
        }
    }


    /**
     * @description: 字符串转希望的JsonObject
     * @param: * @param data
     * @return: net.sf.json.JSONObject
     * @author JJ
     * @date: 2020/9/27 9:26
     */
    public static JSONObject StringToJsonObject(String data) {
        List<String> infos = new ArrayList<>();
        infos.add(data);
        Map map = new HashMap();
        map.put("data", infos);
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }

    /**
     * @param pageNo
     * @param pageSize
     * @description: 分页查找文件
     * @param: * @param sourceFile
     * @return: void
     * @author JJ
     * @date: 2020/9/27 17:03
     */
    public static List<String> readForPage(File sourceFile, int pageNo, int pageSize) throws IOException {
        List<String> result = new ArrayList<>();
        FileReader in = new FileReader(sourceFile);
        LineNumberReader reader = new LineNumberReader(in);
        String s = "";
		/*if (lineNumber <= 0 || lineNumber > getTotalLines(sourceFile)) {
		    log.info("不在文件的行数范围(1至总行数)之内。");
		    System.exit(0);
		}  */
        int startRow = (pageNo - 1) * pageSize + 1;
        int endRow = pageNo * pageSize;
        int lines = 0;
        while (s != null) {
            lines++;
            s = reader.readLine();
            if (lines >= startRow && lines <= endRow) {
                if (s != null) {
                    result.add(0, s);
                } else {
                    //当读到空值时，不算总数
                    lines--;
                }
            }
        }
        reader.close();
        in.close();
        return result;
    }

    /**
     * @param pageNo
     * @param allInfo
     * @description: 判断消息是否为新消息，闭包思想，采用分页器，减少消息大时的内存占用
     * @param: * @param chatList
     * @return: com.beped.chat.domain.ChatList
     * @author JJ
     * @date: 2020/9/28 9:24
     */
    public static ChatList isNewInfo(ChatList chatList, int pageNo, List<String> allInfo) throws IOException {
        //判断消息是否是新消息，通过时间戳
        log.info("查看全部获取的消息" + allInfo);
        //判断聊天是否存在
        try {
            if (allInfo.size() > 0) {
                String infos = allInfo.get(0);
                //判断本页最旧聊天信息是否存在
                Message lastInfo = jsonStrToJavas(infos);
                log.info("本页最老的消息" + allInfo.get(0));
                log.info("本页的最后一条信息" + (lastInfo.getTimestamp() > chatList.getLast_modify_time()));
                //聊天文件获取页的最旧一条消息是否大于聊天列表最新时间戳
                if (lastInfo.getTimestamp() >= chatList.getLast_modify_time()) {
                    pageNo++;
                    List<String> allInfos = WebsocketUtilsBat.getMessage(chatList.getFile_name(), pageNo, 20);
                    if (allInfos != null) {
                        chatList.setInfo_num(chatList.getInfo_num() + allInfo.size());
                        isNewInfo(chatList, pageNo, allInfos);
                    }
                    return chatList;
                }
                //由于最新消息在末尾，所以从末尾开始循环判断，减少运算
                log.info("查看消息列表的时间戳" + chatList.getLast_modify_time());
                for (int i = allInfo.size() - 1; i > 0; i--) {
                    Message message = WebSocketUtils.jsonStrToJavas(allInfo.get(i));
                    log.info("查看每条息消的时间戳" + message.getTimestamp());
                    if (chatList.getLast_modify_time() <= message.getTimestamp()) {
                        chatList.setInfo_num(chatList.getInfo_num() + 1);
                    } else {
                        break;
                    }
                }
            }
        } catch (NullPointerException e) {
            log.info("本页最旧消息为空");
            e.printStackTrace();
        }
        return chatList;
    }

    /**
     * @param chatList
     * @description: 将最新消息添加新聊天列表
     * @param: * @param newChat
     * @return: com.beped.chat.domain.ChatList
     * @author JJ
     * @date: 2020/9/28 9:23
     */
    public static ChatList setChat_record(List<String> newChat, ChatList chatList) throws UnsupportedEncodingException {
        if (newChat == null) {
            log.info("聊天文件不存在");
            return null;
        } else {
            log.info("每个聊天列表的信息" + newChat.get(0));
            //判断第一条内容是否存在
            if (newChat.get(0).length() > 0) {
                Message messages = WebSocketUtils.jsonStrToJavas(newChat.get(0));
                chatList.setChat_record(URLDecoder.decode(messages.getMsg(), "utf-8"));
                return chatList;
            } else {
                return null;
            }
        }
    }

    /**
     * @description: 封装好给前端的消息
     * @param: * @param messages
     * @return: net.sf.json.JSONObject
     * @author JJ
     * @date: 2020/9/28 10:11
     */
    public static JSONObject packInfo(List<String> messages) {
        Map map = new HashMap();
        map.put("data", messages);
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }
}
