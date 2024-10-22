package com.gatewayservice.auth.entitiy;

import lombok.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table("member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Column("name")
    private String name;

    @Column("email")
    private String email;

    @Column("password")
    private String password;

    @Column("birth_date")
    private LocalDate birthDate;

    @Column("inflow")
    private String inflow;

    @Column("gender")
    private String gender;

    @Column("nickname")
    private String nickname;

    @Column("role")
    private MemberRole role;

    @Column("state")
    private MemberState state;

    @Builder
    public Member(
            String name, String email, String password, LocalDate birthDate,
            String inflow, String gender, String nickname, MemberRole role,
            MemberState state
    ) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.inflow = inflow;
        this.gender = gender;
        this.nickname = nickname;
        this.role = role;
        this.state = state;
    }

    public static Member of(
            String name, String email, String password, LocalDate birthDate,
            String inflow, String gender, String nickname, MemberRole role,
            MemberState state
    ) {
        return Member.builder()
                .name(name)
                .email(email)
                .password(password)
                .birthDate(birthDate)
                .inflow(inflow)
                .gender(gender)
                .nickname(nickname)
                .role(role)
                .state(state)
                .build();
    }
}
