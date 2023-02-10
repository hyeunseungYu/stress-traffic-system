package com.project.stress_traffic_system.order.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class OrderDto {

    private Long orderId;
    private LocalDateTime orderDate;
}
