package com.bbkj.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO
 * @date 2020/11/16 9:38
 */
@Setter
@Getter
@Entity
@Table(name = "t_return_img")
public class ReturnImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private long return_id;
    @Column
    private String return_img;

}
