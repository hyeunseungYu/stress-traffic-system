package com.project.stress_traffic_system.event.service;

import com.project.stress_traffic_system.event.model.CouponEventWinner;
import com.project.stress_traffic_system.event.repository.CouponEventWinnerRepository;
import com.project.stress_traffic_system.product.model.dto.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EventSchedulingService {

    private static String USER_QUEUE_PREFIX = "WINNER_";
    private final RedisTemplate<String,String> userQueue;
    private final CouponEventWinnerRepository couponEventWinnerRepository;

    @Scheduled(fixedDelay = 120000) // 2분 마다 실행
    public void winnersRedisToMySQL() {
        ZSetOperations<String, String> winners = userQueue.opsForZSet();
        ScanOptions options = ScanOptions.scanOptions().match(USER_QUEUE_PREFIX + "*").count(10000).build();
        Cursor<byte[]> keys = scanKeys(options);

        List<String> result = new ArrayList<>();
        while (keys.hasNext()) {
            String key = new String(keys.next());
            String username = key.split("_")[1];
            Set<String> values = winners.range(key, 0, -1);
            for (String value : values) {
                Double score = winners.score(key, value);
                CouponEventWinner user = new CouponEventWinner();
                user.setEventDate(score);
                user.setCouponType(value);
                user.setUsername(username);
                couponEventWinnerRepository.save(user);
                winners.remove(key, winners.range(key, 0, -1).toArray(new String[0]));
            }
        }
    }

    private Cursor<byte[]> scanKeys(ScanOptions options) {
        return userQueue.getConnectionFactory().getConnection().scan(options);
    }
}
