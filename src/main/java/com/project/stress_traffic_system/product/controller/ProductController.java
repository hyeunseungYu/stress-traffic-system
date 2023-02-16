package com.project.stress_traffic_system.product.controller;

import com.project.stress_traffic_system.product.model.dto.ProductResponseDto;
import com.project.stress_traffic_system.product.model.dto.ProductSearchCondition;
import com.project.stress_traffic_system.product.model.dto.ReviewRequestDto;
import com.project.stress_traffic_system.product.model.dto.ReviewResponseDto;
import com.project.stress_traffic_system.security.UserDetailsImpl;
import com.project.stress_traffic_system.product.service.ProductService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ProductController {

    private final ProductService productService;

    @ApiOperation(value = "상품전체조회", notes = "10개씩 페이징")
    @GetMapping("/products")
    public Page<ProductResponseDto> getProducts(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam("page") int page) {
        return productService.getProducts(userDetails.getMember(), page);
    }

    @ApiOperation(value = "상품 상세페이지")
    @GetMapping("/products/{productId}")
    public ProductResponseDto getProductDetail(@PathVariable Long productId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return productService.getProduct(userDetails.getMember(), productId);
    }

    @ApiOperation(value = "상품검색", notes = "이름, 가격 범위로 필터링")
    @GetMapping("/products/search")
    public Page<ProductResponseDto> searchProducts(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            ProductSearchCondition condition) {
        return productService.searchProducts(userDetails.getMember(), condition);
    }

    @ApiOperation(value = "카테고리1 상품조회")
    @GetMapping("/products/category-1")
    public Page<ProductResponseDto> searchByCategory1(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam("page") int page) {
        return productService.searchByCategory(userDetails.getMember(), 1L, page);
    }

    @ApiOperation(value = "카테고리2 상품조회")
    @GetMapping("/products/category-2")
    public Page<ProductResponseDto> searchByCategory2(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam("page") int page) {
        return productService.searchByCategory(userDetails.getMember(), 2L, page);
    }

    @ApiOperation(value = "카테고리3 상품조회")
    @GetMapping("/products/category-3")
    public Page<ProductResponseDto> searchByCategory3(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam("page") int page) {
        return productService.searchByCategory(userDetails.getMember(), 3L, page);
    }

    @ApiOperation(value = "카테고리4 상품조회")
    @GetMapping("/products/category-4")
    public Page<ProductResponseDto> searchByCategory4(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam("page") int page) {
        return productService.searchByCategory(userDetails.getMember(), 4L, page);
    }

    @ApiOperation(value = "카테고리5 상품조회")
    @GetMapping("/products/category-5")
    public Page<ProductResponseDto> searchByCategory5(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam("page") int page) {
        return productService.searchByCategory(userDetails.getMember(), 5L, page);
    }

    @ApiOperation(value = "상품 리뷰 등록하기")
    @PostMapping("/products/{productId}/review")
    public ReviewResponseDto createReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long productId, @RequestBody ReviewRequestDto requestDto) {
        return productService.createReview(userDetails.getMember(), productId, requestDto);
    }

    @ApiOperation(value = "상품 리뷰목록 조회하기")
    @GetMapping("/products/{productId}/review")
    public List<ReviewResponseDto> getReviews(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long productId) {
        return productService.getReviews(userDetails.getMember(), productId);
    }
}
