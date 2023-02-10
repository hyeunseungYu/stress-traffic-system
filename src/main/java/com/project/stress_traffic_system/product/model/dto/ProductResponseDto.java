package com.project.stress_traffic_system.product.model.dto;

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
}
