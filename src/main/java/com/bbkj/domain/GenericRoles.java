package com.bbkj.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author JJ0
 * @version 1.0
 * @description: TODO
 * @date 2021/1/18 17:53
 */
@Setter
@Getter
@Entity
@Table(name = "t_generic_roles")
public class GenericRoles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
}