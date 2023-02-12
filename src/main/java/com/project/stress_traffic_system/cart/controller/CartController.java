package com.project.stress_traffic_system.cart.controller;

import com.project.stress_traffic_system.cart.model.dto.CartRequestDto;
import com.project.stress_traffic_system.cart.service.CartService;
import com.project.stress_traffic_system.security.UserDetailsImpl;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class CartController {

    private final CartService cartService;

    @ApiOperation(value = "장바구니에 상품 추가")
    @PostMapping("/products/cart")
    public ResponseEntity<String> addToCart(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CartRequestDto requestDto) {
        cartService.addToCart(userDetails.getMember(), requestDto);
        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    @ApiOperation(value = "장바구니 상품 수량 변경")
    @PatchMapping("/products/{productId}/cart-update/{quantity}")
    public ResponseEntity<String> updateQuantity(
            @PathVariable Long productId,
            @PathVariable int quantity,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        cartService.updateQuantity(userDetails.getMember(), productId, quantity);
        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    @ApiOperation(value = "장바구니 단일 상품 삭제")
    @PatchMapping("/products/{productId}/cart-delete")
    public ResponseEntity<String> deleteProduct(
            @PathVariable Long productId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        cartService.deleteProduct(userDetails.getMember(), productId);
        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    @ApiOperation(value = "장바구니 비우기")
    @PatchMapping("/products/cart-empty")
    public ResponseEntity<String> emptyCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        cartService.emptyCart(userDetails.getMember());
        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }
}
