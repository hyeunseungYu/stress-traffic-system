package com.project.stress_traffic_system.product.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "imgs3")
public class ImgS3 {

    @Id
    @Column(name = "img_id")
    private int id;

    @Column(name = "img_s3")
    private String imgUrl;

}
