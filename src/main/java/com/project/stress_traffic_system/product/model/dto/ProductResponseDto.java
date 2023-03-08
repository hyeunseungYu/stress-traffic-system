package com.project.stress_traffic_system.product.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.project.stress_traffic_system.product.model.Product;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.redis.core.ZSetOperations;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Builder
public class ProductResponseDto {
    private Long id;
    private String name; //상품이름
    private int price; //가격
    private String description; //상세설명
    private int shippingFee; // 배송료
    private int imgurl; //상품이미지
    private Long clickCount; // 조회수
    private Long orderCount;// 주문수량
    private int stock; //상품수량
    private String introduction; //책소개
    private int pages; //쪽수


    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime date;

    @QueryProjection
    public ProductResponseDto(Long id, String name, int price, String description, int shippingFee, int imgurl, Long clickCount, Long orderCount, int stock, String introduction, int pages, LocalDateTime date) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.shippingFee = shippingFee;
        this.imgurl = imgurl;
        this.clickCount = clickCount;
        this.orderCount = orderCount;
        this.stock = stock;
        this.introduction = introduction;
        this.pages = pages;
        this.date = date;
    }

    public static ProductResponseDto convertToResponseRankingDto(ZSetOperations.TypedTuple typedTuple){
        return (ProductResponseDto) typedTuple.getValue();
    }

    public void setClickCount(Long clickCount) {
        this.clickCount = clickCount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public ProductResponseDto(Product product) {
        this.name = product.getName();
        this.id = product.getId();
        this.stock = product.getStock();
        this.price = product.getPrice();
        this.orderCount = product.getOrderCount();
        this.description = product.getDescription();
        this.clickCount = product.getClickCount();
        this.introduction = product.getIntroduction();
        this.date = product.getDate();
    }

}
