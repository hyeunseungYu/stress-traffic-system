package com.project.stress_traffic_system.ticketing.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SeatsResponseDto {
    private long seatId;
    private String seatNum;
    private boolean status;
}
