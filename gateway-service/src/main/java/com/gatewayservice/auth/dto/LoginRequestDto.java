package com.gatewayservice.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginRequestDto {

    @Schema(description = "이메일", example = "dyw1014@gahon.ac.kr")
    @NotBlank
    @Email
    private final String email;

    @Schema(description = "비밀번호", example = "password")
    @NotBlank
    private final String password;

    @Builder
    public LoginRequestDto(
            String email, String password
    ) {
        this.email = email;
        this.password = password;
    }

    public static LoginRequestDto of(String email, String password) {
        return LoginRequestDto.builder().email(email).password(password).build();
    }
}