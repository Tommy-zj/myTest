package com.bbkj.domain;

import com.bbkj.common.GetLocalTime;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO
 * @date 2020/11/11 9:29
 */
@Setter
@Getter
@Entity
@Table(name = "t_shop_orders")
public class ShopOrders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String order_number; //订单号
    @Column
    private long buyer_id; //购买人的id
    @Column
    private String buyer_name; //购买人姓名
    @Column
    private long billing_id; // 发布商品人id
    @Column
    private String pay_time = GetLocalTime.localTime(); //支付时间
    @Column
    private long pay_timestamp = System.currentTimeMillis(); //支付时间戳
    @Column
    private String end_time; //结束时间
    @Column
    private int num; //数量
    @Column(length = 16, scale = 2)
    private double amount; //金额
    @Column(length = 2)
    private int status; //0为支付了定金，待发货，1待收货，2为已签收，3为撤销待处理，4为已撤销，5为用户删除,6为撤销不通过
    @Column
    private Long secondSort_id; //商品的二级分类
    @Column
    private long shop_id; //商品的id
    @Column(length = 32)
    private String shop_name; //商品名称
    @Column
    private String shop_numbering; //商品的型号
    @Column
    private String address; //收货地址
    @Column(length = 12)
    private long phone; // 联系电话
    @Column(length = 64)
    private String delivery;//交货方式
    @Column
    private String remarks;//备注
    @Column
    private String shop_img;//商品图片

}
