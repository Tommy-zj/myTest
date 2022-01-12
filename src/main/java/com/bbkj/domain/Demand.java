package com.bbkj.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "t_demand")
public class Demand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 2)
    private int status; //需求的状态码，0.待审核，1.不通过，2.未解决,3.未付款，4.已付款，5.已解决
    @Column
    private long person_id; //发布需求人id
    @Column(length = 32)
    private String name;//发布人姓名
    @Column(length = 13)
    private long phone;//
    @Column(length = 128)
    private String content;//发布人说明--标题
    @Column(length = 64)
    private String shop_type;//商品类型
    @Column(length = 32)
    private String province_name;//省.
    @Column(length = 32)
    private String city_name;//市
    @Column(length = 32)
    private String county_name;//区
    @Column(length = 128)
    private String remark;//不通过原因
    @Column
    private String description;//说明
    @Column
    private long create_time = System.currentTimeMillis(); //发布时间戳
    @Column(length = 32, scale = 2, precision = 6)
    private double bounty; //赏金
    @Column
    private String address; //详细地址
    @Column
    private String tag;//标签
    @Column
    private boolean disable; //是否禁用，0为false不禁用，1为true禁用
    @Column
    private String remote_user;//发布人ip

    @Override
    public String toString() {
        return "ShopDemand{" +
                "id=" + id +
                ", status=" + status +
                ", person_id=" + person_id +
                ", name='" + name + '\'' +
                ", phone=" + phone +
                ", content='" + content + '\'' +
                ", shop_type='" + shop_type + '\'' +
                ", province_name='" + province_name + '\'' +
                ", city_name='" + city_name + '\'' +
                ", county_name='" + county_name + '\'' +
                ", remark='" + remark + '\'' +
                ", description='" + description + '\'' +
                ", create_time=" + create_time +
                ", bounty=" + bounty +
                ", address='" + address + '\'' +
                ", tag='" + tag + '\'' +
                ", disable=" + disable +
                ", remote_user='" + remote_user + '\'' +
                '}';
    }
}
