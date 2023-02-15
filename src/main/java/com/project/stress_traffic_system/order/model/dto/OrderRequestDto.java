package com.project.stress_traffic_system.order.model.dto;

import lombok.Getter;

@Getter
public class OrderRequestDto {

    private Long productId;
    private Integer quantity;
    private String dcType; /* none(할인없음), price(금액할인), percentage(비율 할인) 중 하나*/
    private Float discount;
}
