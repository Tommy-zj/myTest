package com.bbkj.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO 退款表
 * @date 2020/11/16 9:31
 */
@Setter
@Getter
@Entity
@Table(name = "t_return")
public class Return {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private long person_id;
    @Column(length = 32)
    private String person_name;
    @Column(length = 12)
    private long phone; //联系号码
    @Column(length = 32)
    private String shop_name; //商品名称
    @Column
    private long shop_id;
    @Column(length = 2)
    private int shop_status;  //商品状态，已收货，未收货，未发货
    @Column
    private String return_reason; //退货原因
    @Column(length = 8, scale = 2)
    private double return_money; //退款金额
    @Column(length = 64)
    private int return_mode; //亏款方法
    @Column
    private String order_number; //订单单号
    @Column
    private String pay_time; //交付定金时间
    @Column
    private long return_time = System.currentTimeMillis(); //撤销时间

}
