package com.gatewayservice.auth.dto;

import com.gatewayservice.auth.entitiy.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponseDto {
    @NotNull
    @Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkZSIsImV4cCI6MTYzNjIwNjIwM30.7")
    String accessToken;

    @NotNull
    @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkZSIsImV4cCI6MTYzNjIwNjIwM30.7")
    String refreshToken;

    @Schema(description = "회원 이름", example = "홍길동")
    @NotNull
    String memberName;

    @Schema(description = "회원 권한", example = "ROLE_USER")
    @NotNull
    MemberRole memberRole;

    @Builder
    public LoginResponseDto(
            String accessToken, String refreshToken, String memberName, MemberRole memberRole
    ) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.memberName = memberName;
        this.memberRole = memberRole;
    }

    public static LoginResponseDto of(
            String accessToken, String refreshToken, String memberName, MemberRole memberRole
    ) {
        return LoginResponseDto.builder().accessToken(accessToken).refreshToken(refreshToken).memberName(memberName).memberRole(memberRole).build();
    }

}