package com.gatewayservice.auth.service;

import com.gatewayservice.auth.dto.*;
import com.gatewayservice.auth.entitiy.Member;
import com.gatewayservice.auth.entitiy.MemberRole;
import com.gatewayservice.auth.entitiy.MemberState;
import com.gatewayservice.auth.repository.MemberRepository;
import com.gatewayservice.global.CustomException;
import com.gatewayservice.config.JwtUtil;
import com.gatewayservice.global.CustomResponseCode;
import com.gatewayservice.global.RedisUtil;
import com.gatewayservice.global.ResponseCode;
import lombok.RequiredArgsConstructor;
import reactor.core.scheduler.Schedulers;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final JavaMailSender mailSender;
    private final StringRedisTemplate redisTemplate;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisUtil redisUtil;
    private final Duration expireTime = Duration.ofSeconds(864000); // 2 weeks

    @Override
    public Mono<CustomResponseCode> logout(String accessToken) {
        return Mono.just(jwtUtil.validateToken(accessToken))
                .doOnSuccess(valid -> redisUtil.deleteValue(jwtUtil.getMemberId(accessToken).toString()))
                .thenReturn(CustomResponseCode.LOGOUT_SUCCESS);
    }

    @Override
    public Mono<ReissueResponseDto> reissue(String refreshToken) {
        return Mono.just(jwtUtil.validateToken(refreshToken))
                .flatMap(valid -> getMember(jwtUtil.getMemberId(refreshToken)))
                .map(member -> {
                    MemberInfoDto memberInfo = MemberInfoDto.toDto(member);
                    String newRefreshToken = jwtUtil.createRefreshToken(memberInfo);
                    String newAccessToken = jwtUtil.createAccessToken(memberInfo);
                    redisUtil.deleteValue(jwtUtil.getMemberId(refreshToken).toString());
                    redisUtil.setValues(memberInfo.getId().toString(), newRefreshToken, expireTime);
                    return ReissueResponseDto.of(newAccessToken, newRefreshToken);
                });
    }

    @Override
    public Mono<LoginResponseDto> login(LoginRequestDto loginRequestDto) {
        return memberRepository.findMemberByEmailAndState(loginRequestDto.getEmail(), MemberState.ACTIVE)
                .switchIfEmpty(Mono.error(new CustomException(ResponseCode.MEMBER_NOT_FOUND)))
                .flatMap(member -> {
                    if (!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
                        return Mono.error(new CustomException(ResponseCode.PASSWORD_INCORRECT));
                    }
                    MemberInfoDto memberInfo = MemberInfoDto.toDto(member);
                    String refreshToken = jwtUtil.createRefreshToken(memberInfo);
                    String accessToken = jwtUtil.createAccessToken(memberInfo);
                    redisUtil.setValues(memberInfo.getId().toString(), refreshToken, expireTime);
                    return Mono.just(LoginResponseDto.of(accessToken, refreshToken, member.getName(), member.getRole()));
                });
    }

    @Override
    public Mono<String> checkDuplicate(String email) {
        return memberRepository.findMemberByNicknameAndState(email, MemberState.ACTIVE)
                .flatMap(existingMember -> Mono.error(new CustomException(ResponseCode.NICKNAME_DUPLICATE)))
                .thenReturn(email);
    }

    @Override
    @Transactional
    public Mono<String> signUp(SignUpRequestDto signUpRequestDto) {
        return isMemberExist(signUpRequestDto.getEmail())
                .then(checkDuplicate(signUpRequestDto.getNickname()))
                .then(Mono.defer(() -> {
                    if (!signUpRequestDto.isAgreeTerms()) {
                        return Mono.error(new CustomException(ResponseCode.TERMS_NOT_AGREED));
                    }

                    Member member = Member.of(
                            signUpRequestDto.getName(),
                            signUpRequestDto.getEmail(),
                            passwordEncoder.encode(signUpRequestDto.getPassword()),
                            signUpRequestDto.getBirthDate(),
                            signUpRequestDto.getInflow(),
                            signUpRequestDto.getGender(),
                            signUpRequestDto.getNickname(),
                            MemberRole.USER,
                            MemberState.ACTIVE
                    );

                    // Reactive save
                    return memberRepository.save(member)
                            .map(Member::getEmail); // 저장된 후 회원의 이메일 반환
                }));
    }

    public Mono<Void> isMemberExist(String email) {
        return memberRepository.findMemberByEmailAndState(email, MemberState.ACTIVE)
                .flatMap(existingMember -> Mono.error(new CustomException(ResponseCode.EMAIL_DUPLICATE)))
                .then();
    }




    public Mono<Void> send(String email, String subject, String text, int code) {
        return Mono.defer(() -> Mono.fromCallable(() -> getEmailRequestCount(email))
                        .subscribeOn(Schedulers.boundedElastic())
                        .flatMap(count -> {
                            if (count >= 5) {
                                return Mono.error(new CustomException(ResponseCode.EMAIL_COUNT_EXCEED));
                            }

                            return Mono.fromRunnable(() -> {
                                SimpleMailMessage message = new SimpleMailMessage();
                                message.setTo(email);
                                message.setSubject(subject);
                                message.setText(text);
                                mailSender.send(message); // 메일 전송도 별도 스레드 풀에서 실행
                                saveVerificationCode(email, String.valueOf(code)); // 인증 코드 저장
                                increaseEmailRequestCount(email); // 이메일 요청 횟수 증가
                            }).subscribeOn(Schedulers.boundedElastic()); // 블로킹 메일 전송도 별도 스레드에서 실행
                        }))
                .then(); // Void 반환
    }


    public Mono<Void> verificationEmail(String code, String savedCode) {
        return Mono.defer(() -> {
            if (!code.equals(savedCode)) {
                return Mono.error(new CustomException(ResponseCode.EMAIL_AUTHORIZATION_FAIL));
            }
            return Mono.empty();
        });
    }

    public String getVerificationCode(String email) {
        return redisTemplate.opsForValue().get(email);
    }

    public void saveVerificationCode(String email, String code) {
        redisTemplate.opsForValue().set(email, code, 1, TimeUnit.MINUTES); // 1분 타임아웃
    }

    public void increaseEmailRequestCount(String email) {
        String key = "email_request_count:" + email;

        Long count = redisTemplate.opsForValue().increment(key);

        if (count == null) {
            count = 1L; // 처음 사용할 경우 1로 초기화
            redisTemplate.opsForValue().set(key, "1");
        }
        if (count == 5) {
            redisTemplate.expire(key, 24, TimeUnit.HOURS);
        }
    }


    public long getEmailRequestCount(String email) {
        String key = "email_request_count:" + email;
        String value = redisTemplate.opsForValue().get(key);
        return value != null ? Long.parseLong(value) : 0;
    }

    public Mono<Member> getMember(Long memberId) {
        return memberRepository.findByIdAndState(memberId, MemberState.ACTIVE)
                .switchIfEmpty(Mono.error(new CustomException(ResponseCode.MEMBER_NOT_FOUND)));
    }
}
