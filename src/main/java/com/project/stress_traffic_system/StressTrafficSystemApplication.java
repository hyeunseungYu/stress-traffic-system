package com.project.stress_traffic_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class StressTrafficSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(StressTrafficSystemApplication.class, args);
    }

}
