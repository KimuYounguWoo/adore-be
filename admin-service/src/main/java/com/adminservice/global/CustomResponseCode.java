package com.adminservice.global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CustomResponseCode {
    NICKNAME_DUPLICATE("닉네임이 중복되었습니다."),
    NICKNAME_NOT_DUPLICATE("사용가능한 닉네임입니다."),
    MEMBER_CREATE_SUCCESS("회원 생성에 성공하였습니다."),
    MEMBER_DELETE_SUCCESS("회원 삭제에 성공하였습니다."),
    MEMBER_UPDATE_SUCCESS("회원 수정에 성공하였습니다.");
    private final String message;
}