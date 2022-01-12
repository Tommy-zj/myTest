package com.bbkj.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO 商户账号
 * @date 2021/4/22 11:08
 */
@Entity
@Table(name = "t_merchant")
@Setter
@Getter
public class Merchant {
    @Id
    @GenericGenerator(name = "merchantId", strategy = "assigned")
    @GeneratedValue(generator = "merchantId")
    private long person_id;
    @Column
    private String mail;
    @Column(length = 12)
    private long phone;
    @Column(length = 16)
    private String password;
}
