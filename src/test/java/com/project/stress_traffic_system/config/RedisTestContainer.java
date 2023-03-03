package com.project.stress_traffic_system.config;

import org.junit.jupiter.api.DisplayName;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@DisplayName("Redis Test Containers")
@ActiveProfiles("test")
@Configuration
public class RedisTestContainer {
    private static final String REDIS_DOCKER_IMAGE = "redis:5.0.3-alpine";

    static {    // redis:5.0.3-alpine이라는 이름의 도커이미지를 생성한다.
        GenericContainer<?> REDIS_CONTAINER =
                new GenericContainer<>(DockerImageName.parse(REDIS_DOCKER_IMAGE))
                        .withExposedPorts(6379)
                        .withReuse(true);

        REDIS_CONTAINER.start(); //포트를 6379로 열고 재사용을 허락한다.

        // redis container와 연결하기위해 호스트와 포트를 설정한다.
        System.setProperty("spring.redis.host", REDIS_CONTAINER.getHost());
        System.setProperty("spring.redis.port", REDIS_CONTAINER.getMappedPort(6379).toString());
    }
}
