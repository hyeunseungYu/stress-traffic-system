package com.project.stress_traffic_system.order.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class OrderListDto {

    private Long orderId;
    private LocalDateTime orderDate;
    private String itemName; //대표상품명
    private int totalQuantity; //주문상품 총 건수
    private int totalPrice;

}
