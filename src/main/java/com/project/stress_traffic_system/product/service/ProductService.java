package com.project.stress_traffic_system.product.service;

import com.project.stress_traffic_system.members.entity.Members;
import com.project.stress_traffic_system.product.model.Category;
import com.project.stress_traffic_system.product.model.Product;
import com.project.stress_traffic_system.product.model.Review;
import com.project.stress_traffic_system.product.model.SubCategory;
import com.project.stress_traffic_system.product.model.dto.ProductResponseDto;
import com.project.stress_traffic_system.product.model.dto.ProductSearchCondition;
import com.project.stress_traffic_system.product.model.dto.ReviewRequestDto;
import com.project.stress_traffic_system.product.model.dto.ReviewResponseDto;
import com.project.stress_traffic_system.product.repository.CategoryRepository;
import com.project.stress_traffic_system.product.repository.ProductRepository;
import com.project.stress_traffic_system.product.repository.ReviewRepository;
import com.project.stress_traffic_system.product.repository.SubCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {
    private final ReviewRepository reviewRepository;

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final int PAGE_SIZE = 100;
    private final String SORT_BY = "date";

    //todo param Members 없앨지 정하기

    //전체상품 가져오기
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getProducts(Members member, int page) {

        //모든 상품 100개 단위로 가져오기
        return productRepository.findAllOrderByClickCountDesc(page);

    }

    //상품 id로 상세정보 가져오기
    @Transactional(readOnly = true)
    public ProductResponseDto getProduct(Members member, Long productId) {
        Product product = findProduct(productId);
        product.setClickCount(product.getClickCount() + 1);
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .shippingFee(product.getShippingFee())
                .imgurl(product.getImgurl())
                .count(product.getClickCount())
                .stock(product.getStock())
                .introduction(product.getIntroduction())
                .pages(product.getPages())
                .date(product.getDate())
                .build();

    }

    // 상품 검색하기 (이름, 가격 필터링)

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> searchProducts(ProductSearchCondition condition) {
        return productRepository.searchProducts(condition);
    }
    /*
        카테고리 1~5 각각 조회하는 Api
     */

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> searchByCategory(Members member, Long categoryId, int page) {

        Optional<SubCategory> subCategory = subCategoryRepository.findById(categoryId);

        if (subCategory.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 카테고리 입니다");
        }
        return productRepository.searchByCategory(categoryId, page);
    }

    /*
        카테고리(대분류) API - 국내도서, 해외도서, E-Book
     */
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> findByMainCategory(Members member, String categoryName, int page) {
        Category category = categoryRepository.findByCategoryName(categoryName).orElseThrow(
                () -> new IllegalArgumentException("등록되지 않은 카테고리 입니다"));

        return productRepository.findByMainCategory(category, page);
    }

    //todo 베스트셀러는 order가 가장 많은 순서대로
    //베스트셀러 조회하기
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> findBestSeller(Members member,int page) {
        return productRepository.findBestSeller(page);
    }


    //리뷰 등록하기
    @Transactional
    public ReviewResponseDto createReview(Members member, Long productId, ReviewRequestDto requestDto) {

        log.info("requestDto.getContent = {}", requestDto.getContent());

        //리뷰를 등록할 상품 찾아오기
        Product product = findProduct(productId);

        Review review = new Review(product, member, requestDto.getContent());

        Review savedReview = reviewRepository.save(review);
        return ReviewResponseDto.builder()
                .id(savedReview.getId())
                .content(savedReview.getContent())
                .username(savedReview.getMember().getUsername())
                .createdAt(savedReview.getCreatedAt())
                .build();
    }

    //리뷰 목록 가져오기
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getReviews(Members member, Long productId) {
        Product product = findProduct(productId);

        List<Review> reviewList = reviewRepository.findAllByProduct(product);

        return reviewList.stream().map(review ->
                        ReviewResponseDto.builder()
                                .id(review.getId())
                                .content(review.getContent())
                                .username(review.getMember().getUsername())
                                .createdAt(review.getCreatedAt())
                                .build())
                .collect(Collectors.toList());
    }

    //상품 id로 상품 찾아오기
    private Product findProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다"));
    }
}
