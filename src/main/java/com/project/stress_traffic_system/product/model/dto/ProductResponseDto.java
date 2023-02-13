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
    private String name;
    private String location;
    private String around;
    private String notice;
    private String base;
    private int price;
    private int imgurl; // todo String 으로 변경
    private LocalDateTime date;

    @QueryProjection
    public ProductResponseDto(Long id, String name, String location, String around, String notice, String base, int price, int imgurl, LocalDateTime date) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.around = around;
        this.notice = notice;
        this.base = base;
        this.price = price;
        this.imgurl = imgurl;
        this.date = date;
    }
}
