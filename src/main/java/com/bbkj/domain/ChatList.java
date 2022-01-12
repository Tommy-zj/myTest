package com.bbkj.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO 聊天列表
 * @date 2020/9/21 16:05
 */
@Getter
@Setter
@Entity
@Table(name = "t_chat_list")
public class ChatList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private long other_id; //聊天对方的id
    @Column
    private long person_id; //本人的id
    @Column
    private String name; //聊天对方的名字
    @Column
    private String image_url; //聊天对方头像
    @Column
    private long crate_time = System.currentTimeMillis(); //新建时间戳
    @Column
    private long last_modify_time; //最近访问时间
    @Column(length = 64)
    private String file_name; //聊天记录名称
    @Column
    private int info_num; //聊天消息
    @Column
    private String chat_record;

}
