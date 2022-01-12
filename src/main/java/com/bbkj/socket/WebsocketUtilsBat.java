package com.bbkj.socket;


import com.bbkj.common.SystemGlobal;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO 聊天室工具类备份
 * @date 2020/9/22 15:48
 */
@Slf4j
public class WebsocketUtilsBat {
    /**
     * @param pageSize 每次获取的信息条数
     * @param pageNo   获取的页数
     * @description: 获取对应房间的len条聊天信息
     * @param: * @param room 房间名称
     * @return: java.util.List<java.lang.String>
     * @author JJ
     * @date: 2020/9/21 11:29
     */
    public static List<String> getMessage(String room, int pageNo, int pageSize) throws IOException {
        File file = new File(SystemGlobal.chatUrl + room + ".txt");
        if (!file.exists()) {
            log.info("文件不存在");
            //新建文件
            file.createNewFile();
            log.info("新建聊天文件成功！");
            return null;
        } else {
            log.info("文件存在");
            List<String> data = WebSocketUtils.readForPage(file, pageNo, pageSize);
            return data.size() > 0 ? data : null;
        }
    }
}
