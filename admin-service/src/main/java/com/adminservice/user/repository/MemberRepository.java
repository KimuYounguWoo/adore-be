package com.adminservice.user.repository;

import com.adminservice.user.entity.Member;
import com.adminservice.user.entity.MemberState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Page<Member> findByNicknameContainingAndState(String nickname, MemberState state, Pageable pageable);
    Page<Member> findByEmailContainingAndState (String email, MemberState state, Pageable pageable);
    Optional<Member> findByIdAndState(Long id, MemberState state);
    boolean existsMemberByEmail(String email);
    boolean existsMemberByNickname(String nickname);
}
