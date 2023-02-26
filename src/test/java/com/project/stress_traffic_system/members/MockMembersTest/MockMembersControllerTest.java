//package com.project.stress_traffic_system.members.MockMembersTest;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.project.stress_traffic_system.config.WebSecurityConfig;
//import com.project.stress_traffic_system.members.controller.MembersController;
//import com.project.stress_traffic_system.members.dto.LoginRequestDto;
//import com.project.stress_traffic_system.members.dto.MembersResponseMsgDto;
//import com.project.stress_traffic_system.members.dto.SignupRequestDto;
//import com.project.stress_traffic_system.members.service.MembersService;
//import com.project.stress_traffic_system.security.UserDetailsImpl;
//import org.apache.commons.lang3.RandomStringUtils;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.FilterType;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.servlet.http.HttpServletResponse;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
////@SpringBootTest
////@AutoConfigureMockMvc
////원석 생각 : WebMvcTest는 controller 및 mvcTest에 필요한 최소의 bean들만 스캔하여
////WebSecurityConfig의 인가("/api/members/**")를 인식하지 못하는 것 같아 Spring Security를 제외했다.
//@WebMvcTest(controllers = MembersController.class,
//        excludeAutoConfiguration = SecurityAutoConfiguration.class, // 추가
//        excludeFilters = {
//                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfig.class)})
//public class MockMembersControllerTest {
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//    @MockBean
//    private MembersService membersService;
//
//    UserDetailsImpl userDetails;
//
//    @Test
//    @DisplayName("회원가입 테스트")
//    public void signup() throws Exception{
//        //given
//        String username = RandomStringUtils.randomAlphabetic(10);
//        String password = "abcde123?";
//        String address = "서울";
//        HttpServletResponse response = mock(HttpServletResponse.class);
//        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
//                .username(username)
//                .password(password)
//                .address(address)
//                .build();
//
//        MembersResponseMsgDto responseMsgDto = new MembersResponseMsgDto("good",200);
//        when(membersService.signup(any(),any())).thenReturn(responseMsgDto);
//
//        //응답받은 value의 경로
//        String msg = "$.[?(@.msg == '%s')]";
//
//        // when & then
//        mockMvc.perform(post("/api/members/signup")
//                        .content(objectMapper.writeValueAsString(signupRequestDto))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath(msg,"good").exists());
//    }
//
//    @Test
//    @DisplayName("로그인 테스트")
//    public void login() throws Exception {
//        //given
//        //실제 저장 되어 있는 아이디
//        String username = "zser27";
//        String password = "abcde123?";
//
//        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
//                .username(username)
//                .password(password)
//                .build();
//
//        MembersResponseMsgDto responseMsgDto = new MembersResponseMsgDto("good",200);
//        when(membersService.login(any(),any())).thenReturn(responseMsgDto);
//
//        //응답받은 value의 경로
//        String msg = "$.[?(@.msg == '%s')]";
//
//        // when & then
//        mockMvc.perform(post("/api/members/login")
//                        .content(objectMapper.writeValueAsString(loginRequestDto))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath(msg,"good").exists());
//    }
//
//}
