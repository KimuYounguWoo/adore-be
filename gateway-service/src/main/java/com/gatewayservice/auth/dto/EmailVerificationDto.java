package com.gatewayservice.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class EmailVerificationDto {
    @NotBlank
    @Email
    private final String email;

    @NotBlank
    private final String code;


    @Builder
    public EmailVerificationDto(String email, String code) {
        this.email = email;
        this.code = code;
    }

    public static EmailVerificationDto of(String email, String code) {
        return EmailVerificationDto.builder()
                .email(email)
                .code(code)
                .build();
    }
}
