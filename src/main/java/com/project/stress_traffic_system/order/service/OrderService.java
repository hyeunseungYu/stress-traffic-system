package com.project.stress_traffic_system.order.service;

import com.project.stress_traffic_system.members.entity.Members;
import com.project.stress_traffic_system.order.model.Order;
import com.project.stress_traffic_system.order.model.dto.OrderRequestDto;
import com.project.stress_traffic_system.order.model.dto.OrderResponseDto;
import com.project.stress_traffic_system.order.repository.OrderRepository;
import com.project.stress_traffic_system.product.model.Product;
import com.project.stress_traffic_system.product.repository.ProductRepository;
import com.project.stress_traffic_system.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    //단일 상품 주문하기
    @Transactional
    public OrderResponseDto orderOne(Members member, OrderRequestDto requestDto) {

        Product product = productRepository.findById(requestDto.getProductId()).orElseThrow(
                () -> new IllegalArgumentException("상품이 존재하지 않습니다")
        );

        Order order = new Order(member, product, requestDto.getQuantity());

        Order savedOrder = orderRepository.save(order);

        //주문내역 반환
        return OrderResponseDto.builder()
                .orderId(savedOrder.getId())
                .name(savedOrder.getProduct().getName())
                .quantity(savedOrder.getQuantity())
                .price(savedOrder.getProduct().getPrice())
                .totalPrice(savedOrder.getTotalPrice())
                .createdAt(savedOrder.getCreatedAt())
                .build();
    }

    //여러 상품 주문하기
    public List<OrderResponseDto> orderMany(Members member, List<OrderRequestDto> requestDtoList) {

        List<Order> orderList = new ArrayList<>();

        for (OrderRequestDto orderRequestDto : requestDtoList) {
            Product product = productRepository.findById(orderRequestDto.getProductId()).orElseThrow(
                    () -> new IllegalArgumentException("상품이 존재하지 않습니다")
            );
            Order order = new Order(member, product, orderRequestDto.getQuantity());
            orderList.add(order);
        }

        List<Order> orders = orderRepository.saveAll(orderList);
        return orders.stream().map(order ->
                        OrderResponseDto.builder().orderId(order.getId())
                                .name(order.getProduct().getName())
                                .quantity(order.getQuantity())
                                .price(order.getProduct().getPrice())
                                .totalPrice(order.getTotalPrice())
                                .createdAt(order.getCreatedAt())
                                .build())
                .collect(Collectors.toList());
    }
}
