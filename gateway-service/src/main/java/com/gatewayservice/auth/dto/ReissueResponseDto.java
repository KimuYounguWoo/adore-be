package com.gatewayservice.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReissueResponseDto {

    @Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkZSIsImV4cCI6MTYzNjIwNjIwM30.7")
    String accessToken;

    @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkZSIsImV4cCI6MTYzNjIwNjIwM30.7")
    String refreshToken;

    @Builder
    public ReissueResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static ReissueResponseDto of(String accessToken, String refreshToken) {
        return ReissueResponseDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

}