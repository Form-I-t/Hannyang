package com.example.hannyang.jwt;

import com.example.hannyang.member.CustomMemberDetails;
import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String jwtSecretKey;
    @Value("${jwt.accessTokenExpirationTime}")
    private Long jwtAccessTokenExpirationTime;
    @Value("${jwt.refreshTokenExpirationTime}")
    private Long jwtRefreshTokenExpirationTime;

    public String generateAccessToken(Authentication authentication) {
        CustomMemberDetails customMemberDetails = (CustomMemberDetails) authentication.getPrincipal();
        Date expiryDate = new Date(new Date().getTime() + jwtAccessTokenExpirationTime);
        return Jwts.builder()
                .setSubject(customMemberDetails.getUsername())
                .claim("memberId", customMemberDetails.getId())
                .claim("email", customMemberDetails.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecretKey)
                .compact();
    }

    public String generateRefreshToken(Authentication authentication) {
        CustomMemberDetails customMemberDetails = (CustomMemberDetails) authentication.getPrincipal();
        Date expiryDate = new Date(new Date().getTime() + jwtRefreshTokenExpirationTime);
        return Jwts.builder()
                .setSubject(customMemberDetails.getUsername())
                .claim("memberId", customMemberDetails.getId())
                .claim("email", customMemberDetails.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecretKey)
                .compact();
    }

    ///////////////////////////////////////////////////////////////////

    public Long getUserIdFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("memberId", Long.class);
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getEmailFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("email", String.class);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            System.out.println("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            System.out.println("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            System.out.println("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            System.out.println("JWT claims string is empty.");
        }
        return false;
    }

    // 쿠키 사용
    public void createTokenCookie(String token, HttpServletResponse response) {
        // 쿠키 생성
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true); // HttpOnly 설정
        cookie.setSecure(true); // Secure 설정
        cookie.setPath("/"); // 모든 경로에서 쿠키 접근 가능
        cookie.setMaxAge((int) (jwtAccessTokenExpirationTime / 1000)); // 만료 시간 설정

        // 응답에 쿠키 추가
        response.addCookie(cookie);
    }

    public void createRefreshTokenCookie(String refreshToken, HttpServletResponse response) {
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true); // HttpOnly 설정
        refreshCookie.setSecure(true); // Secure 설정
        refreshCookie.setPath("/"); // 모든 경로에서 쿠키 접근 가능
        refreshCookie.setMaxAge((int) (jwtRefreshTokenExpirationTime / 1000)); // 만료 시간 설정 (리프레시 토큰 만료 시간 사용)

        // 응답에 쿠키 추가
        response.addCookie(refreshCookie);
    }

    public void addTokenToHeader(HttpServletResponse response, String tokenName, String tokenValue, Long duration) {
        response.setHeader(tokenName, "Bearer " + tokenValue);
    }
}
