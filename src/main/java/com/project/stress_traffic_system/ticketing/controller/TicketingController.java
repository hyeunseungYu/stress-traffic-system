package com.project.stress_traffic_system.ticketing.controller;

import com.project.stress_traffic_system.security.UserDetailsImpl;
import com.project.stress_traffic_system.ticketing.service.TicketingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class TicketingController {

    private final TicketingService ticketingService;

    // 좌석 정보 가져오기
    @GetMapping("/seats/{showId}")
    public String getSeats(@PathVariable Long showId, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        model.addAttribute("seats", ticketingService.getSeats(showId, userDetails.getMember().getId()));
        return "seats";
    }

    @PostMapping("/seats/{showId}")
    @ResponseBody
    public ResponseEntity<String> reservationSeats(@RequestBody String seats, @PathVariable Long showId, @AuthenticationPrincipal UserDetailsImpl userDetails) { //todo 좌석 번호들을 어떻게 넘길건지 다시 정하기
        log.info("TicketingController - 예약하기 실행");

        ticketingService.reservationSeats(seats, showId, userDetails.getMember().getId());
        return new ResponseEntity<>("예약 성공", HttpStatus.CREATED);
    }
}
