package com.project.stress_traffic_system.order.controller;

import com.project.stress_traffic_system.facade.RedissonLockStockFacade;
import com.project.stress_traffic_system.order.model.dto.OrderDetailDto;
import com.project.stress_traffic_system.order.model.dto.OrderDto;
import com.project.stress_traffic_system.order.model.dto.OrderListDto;
import com.project.stress_traffic_system.order.model.dto.OrderRequestDto;
import com.project.stress_traffic_system.order.service.OrderService;
import com.project.stress_traffic_system.security.UserDetailsImpl;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class OrderController {

    private final OrderService orderService;
    private final RedissonLockStockFacade redissonLockStockFacade;

    @ApiOperation(value = "단일상품 주문하기")
    @PostMapping("/products/order-one")
    public OrderDto orderOne(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody OrderRequestDto requestDto) {

        return orderService.orderOne(userDetails.getMember(), requestDto);
    }

    @ApiOperation(value = "단일상품 주문하기 - pessimistic lock")
    @PostMapping("/products/order-one/pessimisticLock")
    public OrderDto orderOneWithPessimisticLock(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody OrderRequestDto requestDto) {

        return orderService.orderOneWithPessimisticLock(userDetails.getMember(), requestDto);
    }

    @ApiOperation(value = "단일상품 주문하기 - redisson")
    @PostMapping("/products/order-one/redissonLock")
    public OrderDto orderOneWithRedissonLock(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody OrderRequestDto requestDto) {

        return redissonLockStockFacade.orderOne(userDetails.getMember(), requestDto);
    }

    @ApiOperation(value = "여러상품 주문하기(장바구니에서)")
    @PostMapping("/products/order-many")
    public OrderDto orderMany(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody List<OrderRequestDto> requestDtoList) {
        return orderService.orderMany(userDetails.getMember(), requestDtoList);
    }

    @ApiOperation(value = "여러상품 주문하기(장바구니에서) - pessimistic lock")
    @PostMapping("/products/order-many/pessimisticLock")
    public OrderDto orderManyWithPessimisticLock(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody List<OrderRequestDto> requestDtoList) {
        return orderService.orderManyWithPessimisticLock(userDetails.getMember(), requestDtoList);
    }

    @ApiOperation(value = "여러상품 주문하기(장바구니에서) - redisson lock")
    @PostMapping("/products/order-many/redissonLock")
    public OrderDto orderManyWithRedissonLock(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody List<OrderRequestDto> requestDtoList) {
        return orderService.orderManyWithRedissonLock(userDetails.getMember(), requestDtoList);
    }

    @ApiOperation(value = "주문내역 리스트보기")
    @GetMapping("/products/order-list")
    public List<OrderListDto> getOrders(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return orderService.getOrders(userDetails.getMember());
    }

    @ApiOperation(value = "주문 상세내역 보기")
    @GetMapping("/products/order-detail/{orderId}")
    public List<OrderDetailDto> getOrderDetail(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long orderId) {
        return orderService.getOrderDetail(userDetails.getMember(), orderId);
    }


}
