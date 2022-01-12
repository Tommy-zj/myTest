package com.bbkj.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO
 * @date 2021/3/11 20:27
 */
@Setter
@Getter
@Entity
@Table(name = "t_catalog")
public class Catalog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 32)
    private String catalog_name;
    @Column
    private long paren_id;
    @Column
    private String icon;
    @Column(length = 64)
    private String image_name;
    @Column(length = 32)
    private int image_size;
}
