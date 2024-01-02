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
        this.authService.signup(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * 토큰갱신 API
     */
    @GetMapping("/api/v1/auth/refresh")
    public ResponseEntity<?> refresh(@RequestHeader("REFRESH_TOKEN") String refreshToken) {
        String newAccessToken = this.authService.refreshToken(refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(newAccessToken);
    }
}
