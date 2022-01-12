package com.bbkj.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;


import javax.persistence.*;

@Entity
@Table(name = "t_member")
@Setter
@Getter
public class Member {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "assigned")
    @GeneratedValue(generator = "idGenerator")
    private long id;
    @Column(length = 64)
    private String openid;
    @Column(length = 32)
    private String name;
    @Column(length = 64)
    private String mail;
    @Column(length = 13)
    private long phone;
    @Column(length = 132)
    private String introduce; //介绍自己
    @Column(length = 32)
    private String province_name;
    @Column(length = 32)
    private String city_name;
    @Column(length = 32)
    private String county_name;
    @Column
    private long create_time = System.currentTimeMillis();
    @Column(length = 648)
    private String avatar;
    @Column
    private String tag;
    @Column(length = 5)
    private int gender;//性别，默认为0

    @Column(length = 32)
    private String password;

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", openid='" + openid + '\'' +
                ", name='" + name + '\'' +
                ", mail='" + mail + '\'' +
                ", phone=" + phone +
                ", introduce='" + introduce + '\'' +
                ", province_name='" + province_name + '\'' +
                ", city_name='" + city_name + '\'' +
                ", county_name='" + county_name + '\'' +
                ", create_time=" + create_time +
                ", avatar='" + avatar + '\'' +
                ", tag='" + tag + '\'' +
                ", gender=" + gender +
                '}';
    }
}
