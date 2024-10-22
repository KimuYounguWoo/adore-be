package com.gatewayservice.config;

import com.gatewayservice.auth.dto.MemberInfoDto;
import com.gatewayservice.auth.entitiy.MemberRole;
import com.gatewayservice.global.CustomException;
import com.gatewayservice.global.ResponseCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private final SecretKey key;
    private final long accessTokenExpTime;
    private final long refreshTokenExpTime;

    public JwtUtil(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access_exp}") long accessTokenExpTime,
            @Value("${jwt.refresh_exp}") long refreshTokenExpTime
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
        this.refreshTokenExpTime = refreshTokenExpTime;
    }

    // 액세스 토큰 생성
    public String createAccessToken(MemberInfoDto member) {
        return createToken(member, accessTokenExpTime);
    }

    // 리프레시 토큰 생성
    public String createRefreshToken(MemberInfoDto member) {
        return createToken(member, refreshTokenExpTime);
    }

    // 토큰 추출
    public String getToken(ServerHttpRequest request){
        return request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    }


    // Bearer 접두사 제거 후 토큰 반환
    public String resolveToken(ServerHttpRequest request){
        String bearerToken = getToken(request);

        if(!StringUtils.isBlank(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }

    // 인증 객체 생성
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if(claims.get("role") == null){
            throw new CustomException(ResponseCode.INVALID_TOKEN);
        }

        MemberRole role;
        try {
            role = MemberRole.valueOf(claims.get("role").toString());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        }

        Collection<? extends GrantedAuthority> authorities = Arrays
                .stream(claims.get("role").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

        MemberInfoDto user = MemberInfoDto.of(
                Long.parseLong(claims.get("memberId").toString()),
                claims.get("memberName").toString(),
                role
        );

        return new UsernamePasswordAuthenticationToken(user, "", authorities);
    }

    // JWT 토큰 생성
    private String createToken(MemberInfoDto member, long expireTime) {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(expireTime);

        return Jwts.builder()
                .claim("memberName", member.getName())
                .claim("memberId", member.getId())
                .claim("role", member.getRole().toString())
                .issuedAt(Date.from(now.toInstant()))
                .expiration(Date.from(tokenValidity.toInstant()))
                .signWith(key)
                .compact();
    }


    // 토큰에서 memberId 추출
    public Long getMemberId(String token) {
        return parseClaims(token).get("memberId", Long.class);
    }

    // 토큰 검증
    public boolean validateToken(String authToken) {
        try {
            log.info("Starting token validation");
            log.info("getSigningKey: {}", key.getAlgorithm());
            Jwts.parser().verifyWith(key).build().parseSignedClaims(authToken).getPayload();
            log.info("Token is valid");
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature or malformed JWT: {}", e.getMessage());
            throw new CustomException(ResponseCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT: {}", e.getMessage());
            throw new CustomException(ResponseCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT: {}", e.getMessage());
            throw new CustomException(ResponseCode.UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {}", e.getMessage());
            throw new CustomException(ResponseCode.INVALID_HEADER_OR_COMPACT_JWT);
        } catch (Exception e) {
            log.error("Unexpected error during token validation: {}", e.getMessage(), e);
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    // Claims 파싱
    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser().verifyWith(key).build().parseSignedClaims(accessToken).getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims(); // 만료된 토큰이라도 클레임을 반환
        }
    }

}
