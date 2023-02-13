package com.project.stress_traffic_system.order.service;

import com.project.stress_traffic_system.members.entity.Members;
import com.project.stress_traffic_system.order.model.OrderItem;
import com.project.stress_traffic_system.order.model.Orders;
import com.project.stress_traffic_system.order.model.dto.OrderDetailDto;
import com.project.stress_traffic_system.order.model.dto.OrderDto;
import com.project.stress_traffic_system.order.model.dto.OrderListDto;
import com.project.stress_traffic_system.order.model.dto.OrderRequestDto;
import com.project.stress_traffic_system.order.repository.OrderRepository;
import com.project.stress_traffic_system.product.model.Product;
import com.project.stress_traffic_system.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    //단일 상품 주문하기
    @Transactional
    public OrderDto orderOne(Members member, OrderRequestDto requestDto) {

        Product product = productRepository.findById(requestDto.getProductId()).orElseThrow(
                () -> new IllegalArgumentException("상품이 존재하지 않습니다")
        );

        if (requestDto.getQuantity() > product.getStock()) {
            throw new IllegalArgumentException("주문 가능 수량을 초과하였습니다");
        }

        //주문상품 객체 만들기
        OrderItem orderItem = OrderItem.createOrderItem(product, requestDto.getQuantity());
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);

        //주문 객체 생성해서 회원정보와 주문상품 정보 저장
        Orders order = Orders.createOrder(member, orderItems);

        log.info("orderItems 사이즈는 = {}", order.getOrderItems().size());

        //주문정보 저장
        Orders savedOrders = orderRepository.save(order);

        //주문내역 반환 (주문번호와, 주문일자)
        return OrderDto.builder()
                .orderId(savedOrders.getId())
                .orderDate(savedOrders.getCreatedAt())
                .build();
    }

    //여러 상품 주문하기
    @Transactional
    public OrderDto orderMany(Members member, List<OrderRequestDto> requestDtoList) {

        List<Orders> ordersList = new ArrayList<>();
        List<OrderItem> orderItems = new ArrayList<>();

        //주문상품 리스트에 주문상품 목록을 담아준다
        for (OrderRequestDto orderRequestDto : requestDtoList) {
            Product product = productRepository.findById(orderRequestDto.getProductId()).orElseThrow(
                    () -> new IllegalArgumentException("상품이 존재하지 않습니다")
            );

            if (orderRequestDto.getQuantity() > product.getStock()) {
                throw new IllegalArgumentException("주문 가능 수량을 초과하였습니다");
            }

            OrderItem orderItem = OrderItem.createOrderItem(product, orderRequestDto.getQuantity());
            orderItems.add(orderItem);
        }

        //주문 객체 생성해서 회원정보와 주문상품 정보 저장
        Orders order = Orders.createOrder(member, orderItems);

        Orders savedOrder = orderRepository.save(order);

        //주문내역 반환(주문번호와 주문일자)
        return OrderDto.builder()
                .orderId(savedOrder.getId())
                .orderDate(savedOrder.getCreatedAt())
                .build();
    }

    //주문내역 리스트 가져오기
    @Transactional(readOnly = true)
    public List<OrderListDto> getOrders(Members member) {
        List<Orders> orderList = orderRepository.findAllByMembersOrderByCreatedAtAsc(member);
        log.info("orderList 사이즈는 = {}", orderList.size());

        return orderList.stream().map(
                orders -> OrderListDto.builder()
                        .orderId(orders.getId())
                        .orderDate(orders.getCreatedAt())
                        .itemName(getItemName(orders))
                        .totalQuantity(orders.getTotalQuantity())
                        .totalPrice(orders.getTotalPrice())
                        .build()
        ).collect(Collectors.toList());
    }

    //주문 상세내역 가져오기
    @Transactional(readOnly = true)
    public List<OrderDetailDto> getOrderDetail(Members member, Long orderId) {
        Orders order = orderRepository.findById(orderId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 주문입니다")
        );

        if (!order.getMembers().getId().equals(member.getId())) {
            throw new IllegalArgumentException("본인의 주문내역만 볼 수 있습니다");
        }

        List<OrderDetailDto> response = new ArrayList<>();

        //주문내역 안의 각각의 주문 상품들 모두 dto로 가져오기
        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            response.add(OrderDetailDto.builder()
                    .orderId(order.getId())
                    .name(orderItem.getProduct().getName())
                    .quantity(orderItem.getQuantity())
                    .price(orderItem.getProduct().getPrice())
                    .totalPrice(order.getTotalPrice())
                    .orderDate(order.getCreatedAt())
                    .status(order.getStatus())
                    .build());
        }
        return response;
    }

    //주문상품 이름 반환하기
    private String getItemName(Orders orders) {
        if (orders.getOrderItems().size() == 1) {
            return orders.getOrderItems().get(0).getProduct().getName();
        }

        //상품 갯수가 여러개이면 "ㅇㅇ 외" 로 표시
        return orders.getOrderItems().get(0).getProduct().getName() + " 외";
    }
}
