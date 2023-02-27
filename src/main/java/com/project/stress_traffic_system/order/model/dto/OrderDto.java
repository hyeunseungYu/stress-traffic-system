package com.project.stress_traffic_system.order.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class OrderDto {

    private Long orderId;
    private LocalDateTime orderDate;
    private int resultStock;
    private List<Integer> resultStockList;
}
