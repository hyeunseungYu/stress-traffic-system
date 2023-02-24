package com.project.stress_traffic_system.members.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SignupRequestDto {
    private String username;
    private String password;
    private String address;


    public SignupRequestDto(String username, String password, String address) {
        this.username = username;
        this.password = password;
        this.address = address;
    }
}
