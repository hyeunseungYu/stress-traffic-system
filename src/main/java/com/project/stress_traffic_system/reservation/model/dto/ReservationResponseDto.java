package com.project.stress_traffic_system.reservation.model.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ReservationResponseDto {

    private Long reservationId;
    private String showTitle;
    private Long memberId;
    @CreatedDate
    private LocalDateTime createdAt;
    private boolean status;
}
