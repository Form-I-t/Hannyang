package com.example.hannyang.config;

import com.example.hannyang.jwt.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors().configurationSource(corsConfigurationSource()).and() // CORS 설정 추가
                .csrf().disable() // CSRF 토큰 비활성화
                .authorizeHttpRequests(authz -> authz
                        // 해당 요청에 대해 아무나 승인
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**", "/api-docs/**", "/product/**", "/api/v1/member/email/**", "/api/v1/member/nickname/**",
                                "/product/all-products", "/product/products-by-category", "/survey-history/by-survey-number/", "/survey/value-for-money", "/survey/popular",
                                "/survey/paged", "/survey/latest", "/survey/by-points", "/survey/by-deadline", "/api/v1/auth/signup", "/api/v1/auth/login").permitAll()
                        // 관리자만 접근할 수 있는 경로 설정
                        .requestMatchers("/product/create-product", "/survey-history/admin/**").hasRole("ADMIN")
                        // 기타 모든 요청에 대해 승인된 사용자만 허용
                        .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 사용하지 않음
                )
                .addFilterBefore(this.jwtTokenFilter, UsernamePasswordAuthenticationFilter.class) // JWT 필터 추가
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*")); // 여기서 적절한 Origin을 설정하세요.
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token")); // 'Authorization' 헤더를 명시적으로 허용
        configuration.setExposedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token")); // 'Authorization' 헤더를 노출
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
