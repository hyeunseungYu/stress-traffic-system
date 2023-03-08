package com.project.stress_traffic_system.members.dto;

import com.project.stress_traffic_system.members.entity.MembersRoleEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class MemberResponseDto {

    private String address;
    private String username;
    private String email;
    private String nickname;
    private MembersRoleEnum role;
}
