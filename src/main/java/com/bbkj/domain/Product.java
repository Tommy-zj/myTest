package com.bbkj.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "t_product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 12)
    private long phone;
    @Column(scale = 2)
    private double price;
    @Column
    private Long second_catalog_id;
    @Column(length = 32)
    private String name;
    @Column(length = 2)
    private int recommend = 0; //是否推荐
    @Column
    private String numbering; //型号
    @Column
    private String cover_img; // 封面图片
    @Column
    private Long first_catalog_id;
    @Column
    private boolean disable; //是否禁用，0为false不禁用，1为true禁用
    @Column
    private int browsing_history = 0; // 商品浏览历史
    @Column
    private long person_id;
    @Column(length = 3)
    private int status; //商品的状态，0.为待审核，1为审核通过
    @Column(length = 128)
    private String remark;
    /*
     创建的时间戳
     **/
    @Column
    private Long create_time;
}
