package com.gatewayservice.auth.service;

import com.gatewayservice.auth.dto.LoginRequestDto;
import com.gatewayservice.auth.dto.LoginResponseDto;
import com.gatewayservice.auth.dto.ReissueResponseDto;
import com.gatewayservice.auth.dto.SignUpRequestDto;
import com.gatewayservice.auth.entitiy.Member;
import com.gatewayservice.global.CustomResponseCode;
import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<LoginResponseDto> login(LoginRequestDto loginRequestDto);
    Mono<ReissueResponseDto> reissue(String refreshToken);
    Mono<CustomResponseCode> logout(String accessToken);
    Mono<String> signUp(SignUpRequestDto signInRequestDto);
    Mono<String> checkDuplicate(String nickname);
    Mono<Void> send(String email, String subject, String text, int code);
    String getVerificationCode(String email);
    void saveVerificationCode(String email, String code);
    void increaseEmailRequestCount(String email);
    long getEmailRequestCount(String email);
    Mono<Void> verificationEmail(String code, String savedCode);
    Mono<Member> getMember(Long memberId);
}