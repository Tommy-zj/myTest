package com.bbkj.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "t_adress")

public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private Long person_id;
    @Column(length = 32)
    private int postal_code;
    @Column(length = 32)
    private Long tel_number;
    @Column(length = 3)
    private int checks = 0;
    @Column(length = 32)
    private String user_name;
    @Column(length = 32)
    private String province_name;
    @Column(length = 32)
    private String city_name;
    @Column(length = 32)
    private String county_name;
    @Column(length = 132, name = "t_address")
    private String detailed_adress;
}
