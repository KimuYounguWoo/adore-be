package com.adminservice.user.entity;

import com.adminservice.global.BaseEntity;
import com.adminservice.user.dto.MemberCreateRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Table(name = "member")
@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Column(name="name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "inflow")
    private String inflow;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name="role", nullable = false)
    private MemberRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
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


