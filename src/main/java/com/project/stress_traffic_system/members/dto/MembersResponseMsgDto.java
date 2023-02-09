package com.project.stress_traffic_system.members.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class MembersResponseMsgDto {
    private String msg;
    private int httpStatus;

    public MembersResponseMsgDto(String msg, int httpStatus) {
        this.msg = msg;
        this.httpStatus = httpStatus;
    }

}
