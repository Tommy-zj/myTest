package com.bbkj.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO 用户登陆注册对象
 * @date 2021/1/18 16:48
 */
@Setter
@Getter
@Entity
@Table(name = "t_user")
public class User {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "assigned")
    @GeneratedValue(generator = "idGenerator")
    private long id;

    @Column(length = 11)
    private String password;

    @Column
    private String email;

    @Column
    private String avatar;

    @Column(length = 32)
    private String name;

    @Override
    public String toString() {
        return "User{" +
                "password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", avatar='" + avatar + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
