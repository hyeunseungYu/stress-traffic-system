package com.project.stress_traffic_system.event.controller;

import com.project.stress_traffic_system.event.dto.CouponEventWinnerDto;
import com.project.stress_traffic_system.event.model.CouponEventWinner;
import com.project.stress_traffic_system.event.service.EventService;
import com.project.stress_traffic_system.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class EventController {

    private final EventService eventService;

    //@ApiOperation(value = "선착순 이벤트 신청하기")
    @PostMapping("/event/{eventId}")
    public ResponseEntity<String> event(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long eventId) {
        String msg = eventService.applyEvent(userDetails.getMember(), eventId);
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }

    //@ApiOperation(value = "선착순 이벤트 상품 등록하기")
    @PostMapping("/event-register/{productId}")
    public ResponseEntity<String> createEvent(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long productId) {
        eventService.createEvent(userDetails.getMember(), productId);
        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    @GetMapping("/event/couponWinner")
    public List<CouponEventWinnerDto> couponWinner(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return eventService.couponWinner(userDetails.getMember());
    }
}
