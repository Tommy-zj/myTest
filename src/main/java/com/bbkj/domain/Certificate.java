package com.bbkj.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "t_certificate")
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long person_id;
    @Column(length = 32)
    private String name;
    @Column
    private String image_url;
    @Column(length = 32)
    private String image_name;
    @Column(length = 32)
    private int image_size;
    @Column(length = 5)
    private int state = 0; //0为待审核，1为通过，2为不通过
    @Column
    private String remark; //不通过原因,备注


}
