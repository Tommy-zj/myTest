package com.bbkj.socket;

import com.bbkj.common.GetLocalTime;
import lombok.Getter;
import lombok.Setter;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO
 * @date 2020/9/19 10:59
 */
@Setter
@Getter
public class Message {
    private String msg;
    private String ctime = GetLocalTime.localTime();
    private long create_id;
    private long timestamp = System.currentTimeMillis();
    private long other_id;

    @Override
    public String toString() {
        return "Message{" +
                "msg='" + msg + '\'' +
                ", ctime='" + ctime + '\'' +
                ", create_id=" + create_id +
                ", timestamp=" + timestamp +
                ", other_id=" + other_id +
                '}';
    }
}
