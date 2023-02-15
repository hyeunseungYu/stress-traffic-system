package com.project.stress_traffic_system.members.controller;

import com.project.stress_traffic_system.members.dto.LoginRequestDto;
import com.project.stress_traffic_system.members.dto.SignupRequestDto;
import com.project.stress_traffic_system.members.dto.MembersResponseMsgDto;
import com.project.stress_traffic_system.members.service.MembersService;
import com.project.stress_traffic_system.security.SessionConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MembersController {
    private final MembersService membersService;


    @PostMapping("/signup")
    public MembersResponseMsgDto signup(@RequestBody SignupRequestDto membersRequestDto, HttpServletResponse response) {
        return membersService.signup(membersRequestDto, response);
    }

    @PostMapping("/login")
    public MembersResponseMsgDto login(@RequestBody LoginRequestDto membersRequestDto, HttpServletResponse response) {
        return membersService.login(membersRequestDto, response);
    }

    @PostMapping("/logout")
    public MembersResponseMsgDto logout(HttpServletResponse response, HttpServletRequest request) {
        return membersService.logout(response, request);
    }

    @PostMapping("/logTest")
    public void logTest() {
        int test = 0/0;
        log.trace("trace Log");
        log.debug("Debug Log");
        log.info("info Log");
        log.warn("Warn Log");
        log.error("Error Log");
    }
}
