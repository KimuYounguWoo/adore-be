package com.gatewayservice.auth.dto;

import com.gatewayservice.auth.entitiy.Member;
import com.gatewayservice.auth.entitiy.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberInfoDto {
    @Schema(description = "회원 ID", example = "1")
    private Long id;

    @Schema(description = "회원 이름", example = "홍길동")
    private String name;

    @Schema
    private MemberRole role;

    @Builder
    public MemberInfoDto(
            Long id, String name, MemberRole role
    ) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public static MemberInfoDto of(
            Long id, String name, MemberRole role
    ) {
        return MemberInfoDto.builder().id(id).name(name).role(role).build();
    }

    public static MemberInfoDto toDto(Member member) {
        return MemberInfoDto.builder()
                .id(member.getId())
                .name(member.getName())
                .role(member.getRole())
                .build();
    }
}