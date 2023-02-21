package com.project.stress_traffic_system.members;//package com.project.stress_traffic_system.members;

import com.project.stress_traffic_system.members.dto.LoginRequestDto;
import com.project.stress_traffic_system.members.dto.MembersResponseMsgDto;
import com.project.stress_traffic_system.members.dto.SignupRequestDto;
import com.project.stress_traffic_system.members.entity.Members;
import com.project.stress_traffic_system.members.repository.MembersRepository;
import com.project.stress_traffic_system.members.service.MembersService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletResponse;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

//@ExtendWith(MockitoExtension.class) //Mockito 의 가짜 객체를 사용
@SpringBootTest
public class MembersServiceTest {

    @Autowired
    MembersService memberService;

    @Test
    @DisplayName("회원가입 기능 확인")
    public void signup() throws Exception {
        //given
        String username = "asdf1234";
        String password = "abcde123?";
        String address = "서울";

        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .username(username)
                .password(password)
                .address(address)
                .build();

        HttpServletResponse response = mock(HttpServletResponse.class);

        //when
        MembersResponseMsgDto responseMsgDto = memberService.signup(signupRequestDto, response);

        //then
        assertThat(responseMsgDto.getMsg()).isEqualTo("회원가입 성공");
    }

    @Test
    @DisplayName("로그인 기능 확인")
    public void login() throws Exception {
        //given
        String username = "asdf1234";
        String password = "abcde123?";
        String address = "서울";

        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .username(username)
                .password(password)
                .build();

        HttpServletResponse response = mock(HttpServletResponse.class);

        //when
        MembersResponseMsgDto responseMsgDto = memberService.login(loginRequestDto, response);

        //then
        assertThat(responseMsgDto.getMsg()).isEqualTo("로그인 성공");
    }

}