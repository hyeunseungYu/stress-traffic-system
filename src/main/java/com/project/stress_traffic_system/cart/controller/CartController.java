package com.project.stress_traffic_system.cart.controller;

import com.project.stress_traffic_system.cart.model.dto.CartResponseDto;
import com.project.stress_traffic_system.cart.service.CartService;
import com.project.stress_traffic_system.security.UserDetailsImpl;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class CartController {

    private final CartService cartService;

    @ApiOperation(value = "장바구니 상품목록 조회")
    @GetMapping("/products/cart")
    public List<CartResponseDto> getCartItems(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return cartService.getCartItems(userDetails.getMember());
    }

    @ApiOperation(value = "장바구니에 상품 추가")
    @PostMapping("/products/cart/{productId}")
    public ResponseEntity<String> addToCart(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long productId) {
        cartService.addToCart(userDetails.getMember(), productId);
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
    @DeleteMapping("/products/{productId}/cart-delete")
    public ResponseEntity<String> deleteProduct(
            @PathVariable Long productId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        cartService.deleteProduct(userDetails.getMember(), productId);
        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    @ApiOperation(value = "장바구니 비우기")
    @DeleteMapping("/products/cart-empty")
    public ResponseEntity<String> emptyCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        cartService.emptyCart(userDetails.getMember());
        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }
}
