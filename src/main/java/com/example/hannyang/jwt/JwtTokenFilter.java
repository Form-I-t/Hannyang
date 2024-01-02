package com.example.hannyang.jwt;

import com.example.hannyang.member.CustomMemberDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomMemberDetailsService customMemberDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 인증이 필요 없는 경로를 정의
        String[] skipPaths = {"/api/auth/", "/swagger-ui/", "/swagger-ui.html", "/v3/api-docs", "/swagger-resources", "/webjars"};

        // 현재 요청 경로
        String requestPath = request.getRequestURI();

        // 인증이 필요 없는 경로인지 확인
        boolean skip = Arrays.stream(skipPaths).anyMatch(requestPath::startsWith);
        if (!skip) {
            // 인증이 필요한 경로에 대한 처리
            String accessToken = getTokenFromRequest(request);
            if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
                UsernamePasswordAuthenticationToken authentication = getAuthenticationFromToken(accessToken);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 다음 필터로 요청을 넘김
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private UsernamePasswordAuthenticationToken getAuthenticationFromToken(String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        UserDetails userDetails = customMemberDetailsService.loadUserBUserId(userId);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
