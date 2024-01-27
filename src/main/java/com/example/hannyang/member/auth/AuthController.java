package com.example.hannyang.member.auth;


import com.example.hannyang.member.dtos.AuthRequestDto;
import com.example.hannyang.member.dtos.AuthResponseDto;
import com.example.hannyang.member.dtos.MemberRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;

    /**
     * 로그인 API
     */
    @PostMapping("/api/v1/auth/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDto requestDto) {
        AuthResponseDto authResponseDto = this.authService.login(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(requestDto);
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
    public ResponseEntity<?> refresh(@RequestHeader("REFRESH_TOKEN") String refreshToken) {
        String newAccessToken = this.authService.refreshToken(refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(newAccessToken);
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
