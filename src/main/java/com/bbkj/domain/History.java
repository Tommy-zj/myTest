package com.bbkj.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


/**
 * @author JJ
 * @version 1.0
 * @description: 历史记录实体类
 * @date 2020/9/14 16:13
 */
@Setter
@Getter
@Entity
@Table(name = "t_history")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private long person_id;
    @Column
    private long product_id;
    @Column
    private String ctime;
    @Column
    private Long timestamp = System.currentTimeMillis();
    @Column
    private String product_name;
    @Column
    private double product_price;
    @Column
    private String product_img;
    @Column
    private long second_id;
}
