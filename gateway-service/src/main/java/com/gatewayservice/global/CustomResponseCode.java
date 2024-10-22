package com.gatewayservice.global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CustomResponseCode {
    LOGIN_SUCCESS("로그인에 성공하였습니다."),
    LOGOUT_SUCCESS("로그아웃에 성공하였습니다."),
    REISSUE_SUCCESS("토큰 재발급에 성공하였습니다."),
    EMAIL_SEND_SUCCESS("이메일 전송에 성공하였습니다."),
    EMAIL_AUTHORIZATION_SUCCESS("이메일 인증에 성공하였습니다."),
    NICKNAME_DUPLICATE("닉네임이 중복되었습니다."),
    NICKNAME_NOT_DUPLICATE("닉네임이 중복되지 않았습니다."),
    SIGNUP_SUCCESS("회원가입에 성공하였습니다."),
    SIGNUP_FAIL("회원가입에 실패하였습니다."),
    EMAIL_SEND_FAIL("이메일 전송에 실패하였습니다."),
    EMAIL_AUTHORIZATION_FAIL("이메일 인증에 실패하였습니다.");
    private final String message;
}