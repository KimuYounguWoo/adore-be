package com.gatewayservice.config;


import com.gatewayservice.global.CustomException;
import com.gatewayservice.global.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@RequiredArgsConstructor
@EnableWebFluxSecurity
@Slf4j
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .cors(corsSpec -> corsSpec.configurationSource(exchange -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.addAllowedOrigin("*");
                    config.addAllowedMethod("*");
                    config.addAllowedHeader("*");
                    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                    source.registerCorsConfiguration("/**", config);
                    return config;
                }))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance()) //session STATELESS
                .authorizeExchange(exchanges -> exchanges
                        .anyExchange().permitAll()
                )
                .formLogin(withDefaults())
                .httpBasic(withDefaults())
                .addFilterBefore(JwtFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
        ;
        return http.build();
    }

    private AuthenticationWebFilter JwtFilter() {
        log.info("JwtFilter is called");
        ReactiveAuthenticationManager authenticationManager = Mono::just;

        AuthenticationWebFilter authenticationWebFilter
                = new AuthenticationWebFilter(authenticationManager);
        authenticationWebFilter.setServerAuthenticationConverter(serverAuthenticationConverter());
        log.info("JwtFilter is called");
        log.info(authenticationWebFilter.toString());
        return authenticationWebFilter;
    }

    private ServerAuthenticationConverter serverAuthenticationConverter() {
        return exchange -> {
            String token = jwtUtil.resolveToken(exchange.getRequest());
            if (Objects.isNull(token)) {
                log.info("인증 건너뛰기");
                return Mono.empty(); // 토큰이 없으면 인증 절차 건너뛰기
            }

            log.info("token: {}", token);
            try {
                if (jwtUtil.validateToken(token)) {
                    log.info("Token is valid, proceeding with authentication");
                    return Mono.justOrEmpty(jwtUtil.getAuthentication(token));
                }
            } catch (CustomException e) {
                log.error("Token validation failed: {}", e.getMessage());
                throw new CustomException(ResponseCode.INVALID_TOKEN);
            } catch (Exception e) {
                log.error("Unexpected exception during token validation: {}", e.getMessage(), e);
                throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
            }
            log.info("Token validation failed, returning empty");
            return Mono.empty();
        };
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
