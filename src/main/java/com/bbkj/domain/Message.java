package com.bbkj.domain;


import com.bbkj.common.GetLocalTime;
import lombok.Getter;
import lombok.Setter;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO
 * @date 2020/11/3 15:23
 */
@Setter
@Getter
public class Message {
    private String msg;
    private String ctime = GetLocalTime.localTime();
    private long orders_id;//接单人的id
    private long create_id;
    private long timestamp = System.currentTimeMillis();
    private long other_id;
    private int clickable; //是否可点击 1为可以点击，0为不给点击
    private int IsSystem; //是否为系统通知，0为不是，1为是
}
