package com.project.stress_traffic_system.order.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class OrderResponseDto {

    private Long orderId;
    private String name;
    private Integer quantity;
    private Integer price;
    private Integer totalPrice;
    private LocalDateTime createdAt;
}
