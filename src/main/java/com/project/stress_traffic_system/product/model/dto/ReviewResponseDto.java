package com.project.stress_traffic_system.product.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReviewResponseDto {

    private Long id;
    private String content;
    private String username;
    private LocalDateTime createdAt;
}
