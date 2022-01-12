package com.bbkj.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO
 * @date 2020/12/16 9:17
 */
@Setter
@Getter
@Entity
@Table(name = "t_product_extra")
public class ProductExtra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private Long product_id;
    @Column
    private int count; //销售额
    @Column(length = 32)
    private String province_name;
    @Column(length = 32)
    private String city_name;
    @Column(length = 32)
    private String county_name;
    @Column
    private String detail_address;
    @Column
    private String detail; //详细描述


}
