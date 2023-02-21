package com.project.stress_traffic_system.product.service;

import com.project.stress_traffic_system.product.model.dto.ProductResponseDto;
import com.project.stress_traffic_system.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductRedisService {
    private final ProductRepository productRepository;

    private final RedisTemplate<String, ProductResponseDto> productRedisTemplate;
    private final RedisTemplate<String, String> clickCountRedisTemplate;

    // 카테고리별로(대분류) 상위 1만건 캐싱하기
    public void cacheProducts(List<ProductResponseDto> list, String code) {
        log.info("productRedisService - cacheProducts 실행");

        // RedisTemplate에서 직렬화에 사용될 키 밸류 직렬화 객체를 가져온다.
        RedisSerializer keySerializer = productRedisTemplate.getStringSerializer();
        RedisSerializer valueSerializer = productRedisTemplate.getValueSerializer();

        productRedisTemplate.delete(code + "::");

        // 파이프라인을 실행하여 Bulk Insert와 유사한 작업을 수행한다.
        productRedisTemplate.executePipelined((RedisCallback<Object>) connection -> {

            AtomicReference<Double> score = new AtomicReference<>((double) 0);
            // 전달된 리스트의 각 요소에 대해서 Redis에 키-값 쌍을 저장한다.
            list.forEach(i -> {
                connection.zSetCommands().zAdd(keySerializer.serialize(code + "::"), // 랭킹 정보를 저장할 Redis의 키
                        score.getAndSet(new Double((double) (score.get() + 1))), // 랭킹 score 값(조회수)
                        valueSerializer.serialize(i)); // value - 랭킹 정보 객체를 직렬화하여 저장
            });
            return null;
        });
    }

    // 상품 상세페이지 조회를 위한 조회수 상위 1만건 캐싱
    public void cacheProductsDetail(List<ProductResponseDto> list) {
        log.info("productRedisService - cacheProductsDetail 실행");

        // RedisTemplate에서 직렬화에 사용될 키 밸류 직렬화 객체를 가져온다.
        RedisSerializer keySerializer = productRedisTemplate.getStringSerializer();
        RedisSerializer valueSerializer = productRedisTemplate.getValueSerializer();

        //기존 데이터 삭제 로직
        Set<String> keys = productRedisTemplate.keys("product::*");
        for (String key : keys) {
            productRedisTemplate.delete(key);
        }

        //파이프라인을 실행하여 Bulk Insert와 유사한 작업을 수행한다.
        productRedisTemplate.executePipelined((RedisCallback<Object>) connection -> {

            // 전달된 리스트의 각 요소에 대해서 Redis에 키-값 쌍을 저장한다.
            list.forEach(i -> {
                String key = "product::" + i.getId(); //Redis에 저장할 키
                connection.set(keySerializer.serialize(key), // Redis에 저장할 키 직렬화
                        valueSerializer.serialize(i)); // 랭킹 정보 객체를 직렬화하여 저장
            });
            return null;
        });
    }

    //카테고리별 top 10000 조회
    public Set<ZSetOperations.TypedTuple<ProductResponseDto>> findProductsByCategory(String key, int page) {
        log.info("sorted set에서 검색할 key 값은 = {}", key);
        log.info("sorted set에서 검색할 page 값은 = {}", page);
        Long start = page * 100L;

        // 랭킹 정보를 가져오기 위해 사용할 ZSetOperations 객체를 가져온다.
        ZSetOperations<String, ProductResponseDto> ZSetOperations = productRedisTemplate.opsForZSet();

        // 지정한 key에 대해서 랭킹이 높은 순서대로 해당 페이지의 100개 상품을 가져온다
        //이때 반환되는 값은 ZSetOperations.TypedTuple<ProductResponseDto> 형태의 Set 객체이다.
        return ZSetOperations.rangeWithScores(key, start, start + 99);
    }

    //레디스에서 상품 상세정보 가져오기 (상품 id로 조회)
    public Optional<ProductResponseDto> getProduct(Long productId) {
        String key = "product::" + productId;
        log.info("Redis 실행 게시글 번호는 = {}", productId);
        ProductResponseDto product = productRedisTemplate.opsForValue().get(key);
        return Optional.ofNullable(product);
    }

    //상품 조회수 증가 -> 레디스에 업데이트
    public void incrementView(String key, Long productId) {

        //레디스에서 키 value (String - String) 템플릿을 가져온다
        ValueOperations<String, String> values = clickCountRedisTemplate.opsForValue();
        // key : [clickCount::1] -> value : [1]

        /*  가져온 레디스에서 키 값이 없으면 키 value 생성하여 저장한다
            키는 productId가 포함되어있고, value는 조회수를 repository 에서 가져온다. 유효시간은 60분
            value를 1만큼 증가시킨다(조회수 증가 => increment)
         */
        if(values.get(key) == null) {
            setView(key, String.valueOf(productRepository.getClickCount(productId)), Duration.ofMinutes(60));
            values.increment(key);
        }
        else values.increment(key);
    }

    //레디스에 상품의 조회수 정보를 저장한다
    private void setView(String key, String clickCount, Duration duration) {
        ValueOperations<String, String> values = clickCountRedisTemplate.opsForValue();
        values.set(key, clickCount, duration);
    }

    //todo 조회수를 주기적으로 DB에 업데이트 하는 코드 필요함
}
