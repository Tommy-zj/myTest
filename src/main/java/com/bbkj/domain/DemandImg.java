package com.bbkj.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "t_demand_img")
public class DemandImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 128)
    private String image_url;
    @Column(length = 128)
    private String image_name;
    @Column(length = 32)
    private int image_size;
    private long demand_id;

    @Override
    public String toString() {
        return "DemandImg{" +
                "image_url='" + image_url + '\'' +
                ", image_name='" + image_name + '\'' +
                ", image_size=" + image_size +
                ", demand_id=" + demand_id +
                ", id=" + id +
                '}';
    }
}
