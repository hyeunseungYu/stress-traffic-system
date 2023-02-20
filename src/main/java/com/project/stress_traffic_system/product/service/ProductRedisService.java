package com.project.stress_traffic_system.product.service;

import com.project.stress_traffic_system.product.model.dto.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductRedisService {

    private final RedisTemplate<String, ProductResponseDto> productRedisTemplate;

    // 카테고리별로(대분류) 캐싱하기
    public void cacheProducts(List<ProductResponseDto> list) {

        // RedisTemplate에서 직렬화에 사용될 키 밸류 직렬화 객체를 가져온다.
        RedisSerializer keySerializer = productRedisTemplate.getStringSerializer();
        RedisSerializer valueSerializer = productRedisTemplate.getValueSerializer();

        // 파이프라인을 실행하여 Bulk Insert와 유사한 작업을 수행한다.
        productRedisTemplate.executePipelined((RedisCallback<Object>) connection -> {

            // 전달된 리스트의 각 요소에 대해서 Redis에 키-값 쌍을 저장한다.
            list.forEach(i -> {
                String key = "product::" + i.getId(); //Redis에 저장할 키
                connection.set(keySerializer.serialize(key), // Redis에 저장할 키 직렬화
                        valueSerializer.serialize(i)); // 객체를 직렬화하여 저장
            });
            return null;
        });
    }
}
