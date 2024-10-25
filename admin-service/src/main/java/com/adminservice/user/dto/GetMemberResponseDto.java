package com.adminservice.user.dto;

import com.adminservice.user.entity.Member;
import com.adminservice.user.entity.MemberRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class GetMemberResponseDto {
    private Long id;
    private String name;
    private String email;
    private String nickname;
    private String gender;
    private String inflow;
    private LocalDate birthDate;
    private MemberRole role;
    private String state;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    private GetMemberResponseDto(Long id, String name, String email, String nickname, String gender, String inflow, LocalDate birthDate, MemberRole role, String state, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.gender = gender;
        this.inflow = inflow;
        this.birthDate = birthDate;
        this.role = role;
        this.state = state;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static GetMemberResponseDto getMemberInfo(Member member) {
        return GetMemberResponseDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .gender(member.getGender())
                .inflow(member.getInflow())
                .birthDate(member.getBirthDate())
                .role(member.getRole())
                .state(member.getState().toString())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }
}
