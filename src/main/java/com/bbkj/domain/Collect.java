package com.bbkj.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "t_collect")
public class Collect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private Long person_id;
    @Column
    private String ctime;
    @Column
    private Long product_id;
    @Column(length = 64)
    private String product_name;
    @Column(length = 32, scale = 2)
    private double product_price;
    @Column(length = 32)
    private String product_img;
    @Column(length = 32)
    private Long second_id;
}
