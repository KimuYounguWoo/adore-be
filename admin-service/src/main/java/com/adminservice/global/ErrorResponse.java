package com.adminservice.global;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    private final String code;
    private final String message;

    public static ErrorResponse of(ResponseCode code) {
        return new ErrorResponse(code.getCode(), code.getMessage());
    }

    public static ErrorResponse of(ResponseCode code, Exception e) {
        return new ErrorResponse(code.getCode(), code.getMessage(e));
    }

    public static ErrorResponse of(ResponseCode code, String message) {
        return new ErrorResponse(code.getCode(), code.getMessage(message));
    }
}