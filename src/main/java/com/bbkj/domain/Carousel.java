package com.bbkj.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Setter
@Getter
@Entity
@Table(name = "t_carousel")
public class Carousel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String image_url;
    @Column(length = 16)
    private int image_size;
    @Column(length = 32)
    private String image_name;
}
