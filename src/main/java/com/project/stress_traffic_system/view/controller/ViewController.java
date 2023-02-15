package com.project.stress_traffic_system.view.controller;

import com.project.stress_traffic_system.members.entity.Members;
import com.project.stress_traffic_system.members.entity.MembersRoleEnum;
import com.project.stress_traffic_system.security.SessionConfig;
import com.project.stress_traffic_system.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ViewController {
    private final SessionConfig sessionConfig;
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
//        Members members = (Members) sessionConfig.getSession(request);
//
//        if (members == null) {
//            return new ModelAndView("login");
//        }
        return new ModelAndView("main");
    }

    @GetMapping("/api/main")
    public String goMain() {
        return "redirect:/main";
    }
}
