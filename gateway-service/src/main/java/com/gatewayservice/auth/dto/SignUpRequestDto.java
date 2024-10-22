package com.gatewayservice.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class SignUpRequestDto {

    @Schema(description = "이메일", example = "dyw1014@gahon.ac.kr")
    @NotBlank
    @Email
    private final String email;

    @Schema(description = "이름", example = "김용우")
    @NotBlank
    private final String name;

    @Schema(description = "비밀번호", example = "password")
    @NotBlank
    private final String password;

    @Schema
    @NotBlank
    private final LocalDate birthDate;

    @Schema
    @NotBlank
    private final String nickname;

    @Schema
    @NotBlank
    private final boolean agreeTerms;

    @Schema
    @NotBlank
    private final String inflow;

    @Schema
    @NotBlank
    private final String gender;

    @Schema
    @NotBlank
    private final boolean nicknameDuplicate;


    @Builder
    public SignUpRequestDto(
            String name, String email, String password, LocalDate birthDate, String nickname, boolean agreeTerms, String inflow, String gender, boolean nicknameDuplicate
    ) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.nickname = nickname;
        this.agreeTerms = agreeTerms;
        this.inflow = inflow;
        this.gender = gender;
        this.nicknameDuplicate = nicknameDuplicate;
    }

    public static SignUpRequestDto of(String name, String email, String password, LocalDate birthDate, String nickname, boolean agreeTerms, String inflow, String gender, boolean nicknameDuplicate) {
        return SignUpRequestDto.builder()
                .name(name)
                .email(email)
                .password(password)
                .birthDate(birthDate)
                .nickname(nickname)
                .agreeTerms(agreeTerms)
                .inflow(inflow)
                .gender(gender)
                .nicknameDuplicate(nicknameDuplicate)
                .build();
    }
}