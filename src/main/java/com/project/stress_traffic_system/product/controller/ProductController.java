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
    public Page<ProductResponseDto> getProducts(@RequestParam("page") int page) {
        return productService.getProducts(page);
    }

    @ApiOperation(value = "상품 상세페이지")
    @GetMapping("/products/{productId}")
    public ProductResponseDto getProductDetail(@PathVariable Long productId) {
        return productService.getProduct(productId);
    }

    @ApiOperation(value = "상품검색", notes = "이름, 가격 범위로 필터링")
    @GetMapping("/products/search")
    public Page<ProductResponseDto> searchProducts(
            ProductSearchCondition condition) {
        return productService.searchProducts(condition);
    }

    @ApiOperation(value = "카테고리1 상품조회")
    @GetMapping("/products/category-1")
    public Page<ProductResponseDto> searchByCategory1(@RequestParam("page") int page) {
        return productService.searchByCategory(1L, page);
    }

    @ApiOperation(value = "카테고리2 상품조회")
    @GetMapping("/products/category-2")
    public Page<ProductResponseDto> searchByCategory2(@RequestParam("page") int page) {
        return productService.searchByCategory(2L, page);
    }

    @ApiOperation(value = "카테고리3 상품조회")
    @GetMapping("/products/category-3")
    public Page<ProductResponseDto> searchByCategory3(@RequestParam("page") int page) {
        return productService.searchByCategory(3L, page);
    }

    @ApiOperation(value = "카테고리4 상품조회")
    @GetMapping("/products/category-4")
    public Page<ProductResponseDto> searchByCategory4(@RequestParam("page") int page) {
        return productService.searchByCategory(4L, page);
    }

    @ApiOperation(value = "카테고리5 상품조회")
    @GetMapping("/products/category-5")
    public Page<ProductResponseDto> searchByCategory5(@RequestParam("page") int page) {
        return productService.searchByCategory(5L, page);
    }

    @ApiOperation(value = "국내도서 상품조회")
    @GetMapping("/products/domestic")
    public List<ProductResponseDto> findDomesticProducts(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam("page") int page) {
        return productService.findByMainCategory("국내도서", page);
    }

    @ApiOperation(value = "해외도서 상품조회")
    @GetMapping("/products/foreign")
    public List<ProductResponseDto> findForeignProducts(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam("page") int page) {
        return productService.findByMainCategory("해외도서", page);
    }

    @ApiOperation(value = "E-Book 상품조회")
    @GetMapping("/products/ebook")
    public List<ProductResponseDto> findEbookProducts(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam("page") int page) {
        return productService.findByMainCategory("e-book", page);
    }

    @ApiOperation(value = "베스트셀러 상품조회")
    @GetMapping("/products/bestseller")
    public List<ProductResponseDto> findBestSeller(@RequestParam("page") int page) {
        return productService.findByMainCategory("best", page);
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
    public List<ReviewResponseDto> getReviews(@PathVariable Long productId) {
        return productService.getReviews(productId);
    }

    @ApiOperation(value = "카테고리별 상품데이터 캐싱")
    @GetMapping("/products/cache")
    public void cacheProducts() {
        productService.cacheProducts();
    }

    @ApiOperation(value = "전체카테고리 상품데이터 1만건 캐싱 - 상세페이지 조회 용도")
    @GetMapping("/products/cache/detail")
    public void cacheProductsDetail() {
        productService.cacheProductsDetail();
    }

}
