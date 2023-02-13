package com.project.stress_traffic_system.product.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", updatable = false)
    private Long id;

    private String name; //상품이름
    private String location; //주소
    private String around; //상세주소
    private String notice; //공지
    private String base; //
    private int price; //가격
    private int imgurl; //상품이미지 todo String 으로 변경

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;
    @CreatedDate
    @Column(name = "DATE")
    private LocalDateTime date;
}
