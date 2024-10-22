package com.gatewayservice.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class EmailSendDto {
    @NotBlank
    @Email
    private final String email;

    @Builder
    public EmailSendDto(String email) {
        this.email = email;
    }

    public static EmailSendDto of(String email) {
        return new EmailSendDto(email);
    }
}
