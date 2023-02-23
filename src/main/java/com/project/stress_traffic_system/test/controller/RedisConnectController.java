package com.project.stress_traffic_system.test.controller;

import com.project.stress_traffic_system.test.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/test/connect")
public class RedisConnectController {

    private final RedisService redisService;

    @GetMapping("/redis")
    public String redisTest() {
        redisService.redisString();
        return "test";
    }
}
