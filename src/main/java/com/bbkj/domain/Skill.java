package com.bbkj.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO 技能类
 * @date 2021/2/26 9:29
 */
@Setter
@Getter
@Entity
@Table(name = "t_skill")
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private long paren_id; //父类id
    @Column(length = 32)
    private String name;
}
