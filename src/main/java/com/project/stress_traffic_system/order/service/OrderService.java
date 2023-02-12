package com.project.stress_traffic_system.order.service;

import com.project.stress_traffic_system.members.entity.Members;
import com.project.stress_traffic_system.order.model.Orders;
import com.project.stress_traffic_system.order.model.dto.OrderDto;
import com.project.stress_traffic_system.order.model.dto.OrderRequestDto;
import com.project.stress_traffic_system.order.model.dto.OrderResponseDto;
import com.project.stress_traffic_system.order.repository.OrderRepository;
import com.project.stress_traffic_system.product.model.Product;
import com.project.stress_traffic_system.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
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

        Orders orders = new Orders(member, product, requestDto.getQuantity());

        Orders savedOrders = orderRepository.save(orders);

        //주문내역 반환
        return OrderResponseDto.builder()
                .orderId(savedOrders.getId())
                .name(savedOrders.getProduct().getName())
                .quantity(savedOrders.getQuantity())
                .price(savedOrders.getProduct().getPrice())
                .totalPrice(savedOrders.getTotalPrice())
                .createdAt(savedOrders.getCreatedAt())
                .build();
    }

    //여러 상품 주문하기
    public List<OrderResponseDto> orderMany(Members member, List<OrderRequestDto> requestDtoList) {

        List<Orders> ordersList = new ArrayList<>();

        //각 상품 dto를 order 엔티티로 변경하여 List에 담아준다
        for (OrderRequestDto orderRequestDto : requestDtoList) {
            Product product = productRepository.findById(orderRequestDto.getProductId()).orElseThrow(
                    () -> new IllegalArgumentException("상품이 존재하지 않습니다")
            );
            Orders orders = new Orders(member, product, orderRequestDto.getQuantity());
            ordersList.add(orders);
        }

        List<Orders> orders = orderRepository.saveAll(ordersList);
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

    //주문내역 리스트 가져오기
    public List<OrderDto> getOrders(Members member) {
        List<Orders> orderList = orderRepository.findAllByMembersOrderByCreatedAtAsc(member);
        return orderList.stream().map(
                orders -> OrderDto.builder()
                        .orderId(orders.getId())
                        .orderDate(orders.getCreatedAt())
                        .build()
        ).collect(Collectors.toList());
    }

    //주문 상세내역 가져오기
    public OrderDto getOrderDetail(Members member, Long orderId) {
        Orders order = orderRepository.findById(orderId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 주문입니다")
        );

        if (!order.getMembers().getId().equals(member.getId())) {
            throw new IllegalArgumentException("본인의 주문내역만 볼 수 있습니다");
        }

        return OrderDto.builder()
                .orderId(order.getId())
                .orderDate(order.getCreatedAt())
                .build();
    }
}
