package com.bbkj.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO
 * @date 2020/10/30 11:49
 */
@Entity
@Table(name = "t_control_center")
@Setter
@Getter
public class ControlCenter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 32)
    private long order_number; //订单号
    @Column
    private long orders_id; //接单人的id
    @Column
    private long billing_id; // 发布需求人id
    @Column(length = 3)
    private int status;//订单状态id
    @Column
    private long request_id; //需求的id
    @Column
    private long create_timestamp = System.currentTimeMillis();
    @Column
    private String pay_time;//支付时间
    @Column
    private String end_time; //结束时间
    @Column
    private String billing_name;//发布需求者姓名
    @Column
    private String content;//说明
    @Column
    private double bounty; //赏金
    @Column
    private long pay_timestamp; //支付时间戳
    @Column
    private String address; //收货地址

}
