package com.adminservice.user.dto;

import com.adminservice.user.entity.Member;
import com.adminservice.user.entity.MemberRole;
import com.adminservice.user.entity.MemberState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class MemberCreateRequestDto {



    private String name;
    private String email;
    private String password;
    private LocalDate birthDate;
    private String inflow;
    private String gender;
    private String nickname;
    private MemberState state;
    private MemberRole role;

    public static Member createMember(MemberCreateRequestDto memberCreateRequestDto) {
        return Member.builder()
                .name(memberCreateRequestDto.getName())
                .email(memberCreateRequestDto.getEmail())
                .password(memberCreateRequestDto.getPassword())
                .birthDate(memberCreateRequestDto.getBirthDate())
                .inflow(memberCreateRequestDto.getInflow())
                .gender(memberCreateRequestDto.getGender())
                .nickname(memberCreateRequestDto.getNickname())
                .state(memberCreateRequestDto.getState())
                .role(memberCreateRequestDto.getRole())
                .build();
    }

    public static Member updateMember(Member member, MemberCreateRequestDto memberCreateRequestDto) {
        BeanUtils.copyProperties(memberCreateRequestDto, member); // 필요한 경우 비밀번호 제외
        return member;
    }
}