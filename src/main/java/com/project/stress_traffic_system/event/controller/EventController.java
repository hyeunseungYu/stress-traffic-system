package com.project.stress_traffic_system.event.controller;

import com.project.stress_traffic_system.event.service.EventService;
import com.project.stress_traffic_system.security.UserDetailsImpl;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class EventController {

    private final EventService eventService;

    @ApiOperation(value = "선착순 이벤트")
    @PostMapping("/event")
    public ResponseEntity<String> event(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String msg = eventService.createEvent(userDetails.getMember());
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }
}
