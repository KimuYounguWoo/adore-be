package com.gatewayservice.auth.repository;

import com.gatewayservice.auth.entitiy.Member;
import com.gatewayservice.auth.entitiy.MemberState;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface MemberRepository extends ReactiveCrudRepository<Member, Long> {
    Mono<Member> findByIdAndState(Long id, MemberState state);
    Mono<Member> findMemberByEmailAndState(String email, MemberState state);
    Mono<Member> findMemberByNicknameAndState(String nickname, MemberState state);
}
