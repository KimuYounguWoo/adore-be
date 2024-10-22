package com.gatewayservice.config;

import com.gatewayservice.global.CustomException;
import com.gatewayservice.global.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
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
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance()) //session STATELESS
                .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec
                        .authenticationEntryPoint((exchange, ex) -> Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)))
                        .accessDeniedHandler((exchange, denied) -> Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN))))
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
        ReactiveAuthenticationManager authenticationManager = Mono::just;

        AuthenticationWebFilter authenticationWebFilter
                = new AuthenticationWebFilter(authenticationManager);
        authenticationWebFilter.setServerAuthenticationConverter(serverAuthenticationConverter());
        return authenticationWebFilter;
    }

    private ServerAuthenticationConverter serverAuthenticationConverter(){
        return exchange -> {
            String token = jwtUtil.resolveToken(exchange.getRequest());
            log.info("token: {}", token);
            try {
                if(!Objects.isNull(token) && jwtUtil.validateToken(token)){
                    log.info("if is called");
                    return Mono.justOrEmpty(jwtUtil.getAuthentication(token));
                }
            } catch (CustomException e) {
                throw new CustomException(ResponseCode.INVALID_TOKEN);
            }
            catch (Exception e) {
                log.error("Unexpected exception during token validation: {}", e.getMessage(), e);
                // 일반적인 예외 처리
                throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
            }
            return Mono.empty();
        };
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
