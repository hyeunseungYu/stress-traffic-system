
package com.project.stress_traffic_system.redisLocalTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Redis 기본적인 저장 가능 여부 테스트")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED) // 실제 DB 사용하고 싶을때 NONE 사용
public class RedisBasicTest {
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Test
    void redisConnectionTest() {
        //given
        final String key = "a";
        final String data = "1";



         /* when
         * Valueoperations -> 레디스를 하나의 밸류로 구성된 키, 밸류의 쌍을 저장할 때
         * HashOperations -> 여러 밸류가 있는 키, 밸류 쌍을 저장할 때
         * ListOperations -> 리스트 타입의 데이터 다룰 떄
         * SetOperations -> set으로 데이터 다룰 때
         */


        final ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        //redis에 키, 밸류 저장
        valueOperations.set(key, data);

        //then
        //저장된 데이터에서 key에 해당하는 것의 밸류를 가져옴
        final String s = valueOperations.get(key);

        //서로 일치하는지 확인
        Assertions.assertThat(s).isEqualTo(data);
    }

}
