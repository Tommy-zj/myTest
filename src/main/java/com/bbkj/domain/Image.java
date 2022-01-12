package com.bbkj.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO 商品图片
 * @date 2021/4/19 11:43
 */

@Getter
@Setter
@Entity
@Table(name = "t_product_img")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private Long product_id;
    @Column
    private String image_url;
}
