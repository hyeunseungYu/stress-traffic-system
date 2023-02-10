package com.project.stress_traffic_system.order.controller;

import com.project.stress_traffic_system.order.model.dto.OrderRequestDto;
import com.project.stress_traffic_system.order.model.dto.OrderResponseDto;
import com.project.stress_traffic_system.order.service.OrderService;
import com.project.stress_traffic_system.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class OrderController {

    private final OrderService orderService;

    //단일상품 주문하기
    @PostMapping("/products/order-one")
    public OrderResponseDto orderOne(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody OrderRequestDto requestDto) {

        return orderService.orderOne(userDetails.getMember(), requestDto);
    }

    //여러상품 주문하기(장바구니 등)
    @PostMapping("/products/order-many")
    public List<OrderResponseDto> orderMany(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody List<OrderRequestDto> requestDtoList) {
        return orderService.orderMany(userDetails.getMember(), requestDtoList);
    }
}
