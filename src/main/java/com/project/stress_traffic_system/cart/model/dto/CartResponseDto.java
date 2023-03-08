package com.project.stress_traffic_system.cart.model.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CartResponseDto {

    private String itemName;
    private int imgurl; //todo url어떻게 처리할건지
    private int price;
    private int quantity;
    private Long product_id;
}
