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
    private int price; //가격
    private String description; //상세설명
    private int shippingFee; // 배송료
    private int imgurl; //상품이미지
    private Long count; // 조회수
    private int stock; //상품수량

    private String introduction; //책소개
    private int pages; //쪽수

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @CreatedDate
    private LocalDateTime date;

    //상품 주문 시 재고수량 감소 시키기
    public void removeStock(int quantity) {
        int restStock = this.stock - quantity;
        if (restStock < 0) {
            throw new IllegalArgumentException("재고가 부족합니다"); //todo 예외
        }
        this.stock = restStock;
    }
}
