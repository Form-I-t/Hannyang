package com.example.hannyang.jwt;

import com.example.hannyang.member.CustomMemberDetails;
import io.jsonwebtoken.*;
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
}
