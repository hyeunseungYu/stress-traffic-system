package com.project.stress_traffic_system.view.controller;

import com.project.stress_traffic_system.members.entity.MembersRoleEnum;
import com.project.stress_traffic_system.security.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
public class ViewController {
    @GetMapping("/signup")
    public ModelAndView signupPage() {
        return new ModelAndView("signup");
    }
    @GetMapping("/login")
    public ModelAndView loginPage() {
        return new ModelAndView("login");
    }

    @GetMapping("/")
    public ModelAndView searchPage() {
        return new ModelAndView("index");
    }

    @GetMapping("/main")
    public ModelAndView mainPage() {

        return new ModelAndView("main");
    }
}
