//package com.project.stress_traffic_system.members;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.project.stress_traffic_system.members.dto.LoginRequestDto;
//import com.project.stress_traffic_system.members.dto.SignupRequestDto;
//import org.apache.commons.lang3.RandomStringUtils;
//import org.junit.Test;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//@WebAppConfiguration
//public class MembersControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    AutoCloseable openMocks;
//
//    ObjectMapper objectMapper = new ObjectMapper();
//
//    @BeforeEach
//    public void setup() {
//        openMocks = MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(MembersControllerTest.class).build();
//    }
//
//    @Test
//    @DisplayName("회원가입 테스트")
//    public void signup() throws Exception{
//        //given
//        String username = RandomStringUtils.randomAlphanumeric(10);
//        String password = "abcde123?";
//        String address = "서울";
//
//        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
//                .username(username)
//                .password(password)
//                .address(address)
//                .build();
//
//        // when & then
//        mockMvc.perform(MockMvcRequestBuilders
//                        .post("/api/members/signup")
//                        .content(objectMapper.writeValueAsString(signupRequestDto))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("로그인 테스트")
//    public void login() throws Exception {
//        //given
//        String username = "zser20";
//        String password = "abcd1234?";
//
//        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
//                .username(username)
//                .password(password)
//                .build();
//
//        // when & then
//        mockMvc.perform(MockMvcRequestBuilders
//                        .post("/api/members/login")
//                        .content(objectMapper.writeValueAsString(loginRequestDto))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//    }
//
//}
