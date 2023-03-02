package com.project.stress_traffic_system.product.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@JsonInclude
@AllArgsConstructor
@Getter
public class Response {
    private long totalTime;
    private HttpStatus httpStatus;
    private String msg;
    private Result result;

    public static <T> Response success(long totalTime, String msg, T data) {
        return new Response(totalTime, HttpStatus.OK, msg, new Success<>(data));
    }
}

