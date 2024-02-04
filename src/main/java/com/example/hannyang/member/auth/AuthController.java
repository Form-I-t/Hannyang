package com.example.hannyang.member.auth;


import com.example.hannyang.jwt.JwtTokenProvider;
import com.example.hannyang.member.dtos.AuthRequestDto;
import com.example.hannyang.member.dtos.AuthResponseDto;
import com.example.hannyang.member.dtos.MemberRequestDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${jwt.accessTokenExpirationTime}")
    private Long jwtAccessTokenExpirationTime;
    @Value("${jwt.refreshTokenExpirationTime}")
    private Long jwtRefreshTokenExpirationTime;

    /**
     * 로그인 API
     */
    @PostMapping("/api/v1/auth/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDto requestDto, HttpServletResponse response) {
        AuthResponseDto authResponseDto = this.authService.login(requestDto);


        // 액세스 토큰과 리프레시 토큰을 HTTP 응답 헤더에 추가
        jwtTokenProvider.addTokenToHeader(response, "Access-Token", authResponseDto.getAccessToken(), jwtAccessTokenExpirationTime);
        jwtTokenProvider.addTokenToHeader(response, "Refresh-Token", authResponseDto.getRefreshToken(), jwtRefreshTokenExpirationTime);

        // 로그인 성공 응답 반환 (클라이언트에 쿠키를 포함하여 반환됩니다)
        return ResponseEntity.status(HttpStatus.OK).body("로그인 성공");
    }


    /**
     * 회원가입 API
     */
    @PostMapping("/api/v1/auth/signup")
    public ResponseEntity<?> signup(@RequestBody MemberRequestDto requestDto) {
        try {
            this.authService.signup(requestDto);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("회원가입에 실패했습니다");
        }
    }

    /**
     * 토큰갱신 API
     */
    @GetMapping("/api/v1/auth/refresh")
    public ResponseEntity<?> refresh(@RequestHeader("REFRESH_TOKEN") String refreshToken, HttpServletResponse response) {
        TokenPair tokenPair = this.authService.refreshToken(refreshToken);
        if (tokenPair != null && tokenPair.getAccessToken() != null && tokenPair.getRefreshToken() != null) {
            // 새 액세스 토큰과 리프레시 토큰을 HTTP 응답 헤더에 추가
            jwtTokenProvider.addTokenToHeader(response, "Access-Token", tokenPair.getAccessToken(), jwtAccessTokenExpirationTime);
            jwtTokenProvider.addTokenToHeader(response, "Refresh-Token", tokenPair.getRefreshToken(), jwtRefreshTokenExpirationTime);
            return ResponseEntity.status(HttpStatus.OK).body("토큰 갱신 성공");
        } else {
            // 새 토큰 생성 실패 (예: 리프레시 토큰 만료)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰 갱신 실패");
        }
    }

    /**
     * 로그아웃 API
     */
    @PostMapping("/api/v1/auth/logout")
    public ResponseEntity<?> logout(@RequestHeader("ACCESS_TOKEN") String accessToken) {
        this.authService.logout(accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
