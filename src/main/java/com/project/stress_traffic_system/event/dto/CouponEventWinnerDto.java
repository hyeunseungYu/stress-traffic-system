package com.project.stress_traffic_system.event.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class CouponEventWinnerDto {

    private Long id;
    private String username;
    private Double eventDate;
    private String couponType;
}
