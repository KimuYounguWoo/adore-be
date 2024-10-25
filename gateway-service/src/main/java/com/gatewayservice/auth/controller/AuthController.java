package com.gatewayservice.auth.controller;

import com.gatewayservice.auth.dto.*;
import com.gatewayservice.auth.service.AuthService;
import com.gatewayservice.global.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Tag(name = "[사용자] 인증 및 인가 관련 API", description = "Auth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    private Random random = new Random();

    private final AuthService authService;

    @Operation(summary = "이메일 전송 API", description = "이메일을 전송합니다.")
    @PostMapping("/email-send")
    public Mono<ResponseEntity<ResponseCode>> sendEmail(@RequestBody EmailSendDto email) {
        String subject = "회원가입 인증 메일입니다.";
        int code = this.random.nextInt(9000) + 1000;
        String text = "인증 코드는 " + code + "입니다.";

        return authService.send(email.getEmail(), subject, text, code)
                .then(Mono.just(ResponseEntity.ok(ResponseCode.EMAIL_SEND_SUCCESS)))
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseCode.EMAIL_SEND_FAIL));
    }

    @Operation(summary = "이메일 인증 API", description = "이메일 인증을 수행합니다.")
    @PostMapping("/email-verify")
    public Mono<ResponseEntity<ResponseCode>> verifyEmail(@RequestBody EmailVerificationDto emailVerificationDto) {
        String email = emailVerificationDto.getEmail();
        String code = emailVerificationDto.getCode(); // 사용자가 입력한 코드
        String savedCode = authService.getVerificationCode(email); // redis에 저장된 코드

        return authService.verificationEmail(code, savedCode)
                .then(Mono.just(ResponseEntity.ok(ResponseCode.EMAIL_AUTHORIZATION_SUCCESS)))
                .onErrorReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseCode.EMAIL_AUTHORIZATION_FAIL));
    }

    @Operation(summary = "로그인 API", description = "로그인을 수행합니다.")
    @PostMapping("/login")
    public Mono<ResponseEntity<LoginResponseDto>> login(@RequestBody LoginRequestDto loginRequestDto) {
        return authService.login(loginRequestDto)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    log.error("로그인 실패", e);
                    log.info(e.getMessage());
                        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null));
                }
                ); // 로그인 실패 처리
    }

    @Operation(summary = "닉네임 중복 체크 API", description = "닉네임 중복을 체크합니다.")
    @GetMapping("/check-duplicate")
    public Mono<ResponseEntity<Map<String, String>>> checkDuplicate(@RequestParam String nickname) {
        return authService.checkDuplicate(nickname)
                .map(checkedNickname -> {
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "닉네임 '" + checkedNickname + "'은 사용 가능합니다.");
                    return ResponseEntity.ok(response);
                })
                .onErrorResume(e -> {
                    Map<String, String> response = new HashMap<>();
                    response.put("error", "닉네임 '" + nickname + "'은 이미 사용 중입니다.");
                    return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(response));
                });
    }

    @Operation(summary = "로그아웃 API", description = "로그아웃을 수행합니다.")
    @GetMapping("/logout")
    public Mono<ResponseEntity<ResponseCode>> logout(@RequestHeader("Authorization") String accessToken) {
        return authService.logout(accessToken.substring(7))
                .map(response -> ResponseEntity.ok(ResponseCode.UNAUTHORIZED))
                .onErrorReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null)); // 로그아웃 실패 처리
    }

    @Operation(summary = "토큰 재발급 API", description = "토큰을 재발급합니다.")
    @GetMapping("/reissue")
    public Mono<ResponseEntity<ReissueResponseDto>> reissue(@RequestHeader("Authorization") String refreshToken) {
        return authService.reissue(refreshToken.substring(7))
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null)); // 재발급 실패 처리
    }

    @Operation(summary = "회원 가입 API", description = "회원 가입을 수행합니다.")
    @PostMapping("/sign-up")
    public Mono<ResponseEntity<String>> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        return authService.signUp(signUpRequestDto)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.status(HttpStatus.CONFLICT).body(null)); // 회원가입 실패 처리
    }

    @GetMapping("/test")
    public Mono<ResponseEntity<String>> test() {
        return Mono.just(ResponseEntity.ok("test"));
    }
}
