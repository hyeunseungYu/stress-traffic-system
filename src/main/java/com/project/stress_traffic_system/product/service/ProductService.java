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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {
    private final ReviewRepository reviewRepository;

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final ProductRedisService productRedisService;
    private final int PAGE_SIZE = 100;
    private final String SORT_BY = "date";


    //전체상품 가져오기
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getProducts(int page) {

        //모든 상품 100개 단위로 가져오기
        return productRepository.findAllOrderByClickCountDesc(page);

    }

    //상품 id로 상세정보 가져오기 (레디스에서 먼저 조회하기)
    @Transactional
    public ProductResponseDto getProduct(Long productId) {

        Optional<ProductResponseDto> redisProduct = productRedisService.getProduct(productId);
        if (redisProduct.isEmpty()) {
            Product product = findProduct(productId);

            //조회수 늘리기
            productRepository.setClickCount(productId, product.getClickCount() + 1);

            return ProductResponseDto.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .description(product.getDescription())
                    .shippingFee(product.getShippingFee())
                    .imgurl(product.getImgurl())
                    .clickCount(product.getClickCount())
                    .orderCount(product.getOrderCount())
                    .stock(product.getStock())
                    .introduction(product.getIntroduction())
                    .pages(product.getPages())
                    .date(product.getDate())
                    .build();
        }

//        addClickCount(productId); todo redis에 저장하고 DB 업데이트 스케줄링 하는 것으로 추후 변경
        long clickCount = redisProduct.get().getClickCount() + 1;
        productRepository.setClickCount(productId, clickCount);
        return redisProduct.get();
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
    public Page<ProductResponseDto> searchByCategory(Long categoryId, int page) {

        Optional<SubCategory> subCategory = subCategoryRepository.findById(categoryId);

        if (subCategory.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 카테고리 입니다");
        }
        return productRepository.searchByCategory(categoryId, page);
    }

    /*
        카테고리(대분류) API - 국내도서, 해외도서, E-Book
     */

    //카테고리별 상품리스트 가져오기
    // Redis에서 조회하고 없을 경우에는 DB 조회하기
    @Transactional(readOnly = true)
    public List<ProductResponseDto> findByMainCategory(String categoryName, int page) {
        Set<ZSetOperations.TypedTuple<ProductResponseDto>> products = productRedisService.findProductsByCategory(convertCategoryName(categoryName) + "::", page);

        log.info("Redis에서 조회한 products 사이즈는 = {}", products.size());

        if (products.size() == 0) {
            Category category = categoryRepository.findByCategoryName(categoryName).orElseThrow(
                    () -> new IllegalArgumentException("등록되지 않은 카테고리 입니다"));

            if (categoryName.equals("best")) {
                return productRepository.findBestSeller(page);
            }
            return productRepository.findByMainCategory(category, page);
        }

        return products.stream().map(ProductResponseDto::convertToResponseRankingDto).collect(Collectors.toList());

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
    public List<ReviewResponseDto> getReviews(Long productId) {
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

    /*
        캐싱
     */

    //카테고리id로 국내도서, 해외도서, EBook 캐싱하기(조휘수 상위 1만개)
    @Transactional
    public void cacheProducts() {
        List<ProductResponseDto> domesticProducts = productRepository.findByMainCategory(1L);
        productRedisService.cacheProducts(domesticProducts, "domestic");

        List<ProductResponseDto> foreignProducts = productRepository.findByMainCategory(2L);
        productRedisService.cacheProducts(foreignProducts, "foreign");

        List<ProductResponseDto> EBookProducts = productRepository.findByMainCategory(3L);
        productRedisService.cacheProducts(EBookProducts, "ebook");

        List<ProductResponseDto> BestSellers = productRepository.findBestSeller();
        productRedisService.cacheProducts(BestSellers, "best");
    }

    //상품 상세페이지 상위 1만건 캐싱하기
    @Transactional
    public void cacheProductsDetail() {
        List<ProductResponseDto> list = productRepository.findProductDetail();
        productRedisService.cacheProductsDetail(list);
    }

    //한글 대분류 이름을 영어로 변환
    private String convertCategoryName(String categoryName) {
        switch (categoryName) {
            case "국내도서":
                return "domestic";
            case "해외도서":
                return "foreign";
            case "e-book":
                return "ebook";
            case "best":
                return "best";
        }
        return categoryName;
    }

    // 상품 조회수 추가로직
    public void addClickCount(Long productId) {
        String key = "clickCount::" + productId;
        log.info("조회수 증가 메서드 실행");
        productRedisService.incrementView(key, productId);
    }
}
