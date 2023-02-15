package com.project.stress_traffic_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableJpaAuditing
@EnableRedisHttpSession
@SpringBootApplication
public class StressTrafficSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(StressTrafficSystemApplication.class, args);
    }

}
