package com.project.stress_traffic_system.product.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Builder
public class ProductResponseDto {
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
    private LocalDateTime date;

    @QueryProjection
    public ProductResponseDto(Long id, String name, int price, String description, int shippingFee, int imgurl, Long count, int stock, String introduction, int pages, LocalDateTime date) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.shippingFee = shippingFee;
        this.imgurl = imgurl;
        this.count = count;
        this.stock = stock;
        this.introduction = introduction;
        this.pages = pages;
        this.date = date;
    }
}
