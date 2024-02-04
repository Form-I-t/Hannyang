package com.example.hannyang.member.auth;


import com.example.hannyang.jwt.JwtTokenProvider;
import com.example.hannyang.member.CustomMemberDetails;
import com.example.hannyang.member.Member;
import com.example.hannyang.member.MemberRepository;
import com.example.hannyang.member.Role;
import com.example.hannyang.member.dtos.AuthRequestDto;
import com.example.hannyang.member.dtos.AuthResponseDto;
import com.example.hannyang.member.dtos.MemberRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 로그인
     */
    @Transactional
    public AuthResponseDto login(AuthRequestDto requestDto) {
        Member member = this.memberRepository.findByEmail(requestDto.getEmail()).orElseThrow(
                () -> new IllegalArgumentException("해당 이메일을 가진 유저를 찾을 수 없습니다. email = " + requestDto.getEmail()));
        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다. email = " + requestDto.getEmail());
        }

        // GENERATE ACCESS_TOKEN AND REFRESH_TOKEN
        String accessToken = this.jwtTokenProvider.generateAccessToken(
                new UsernamePasswordAuthenticationToken(new CustomMemberDetails(member), member.getPassword()));
        String refreshToken = this.jwtTokenProvider.generateRefreshToken(
                new UsernamePasswordAuthenticationToken(new CustomMemberDetails(member), member.getPassword()));

        // CHECK IF AUTH ENTITY EXISTS, THEN UPDATE TOKEN
        if (this.authRepository.existsByMember(member)) {
            member.getAuth().updateAccessToken(accessToken);
            member.getAuth().updateRefreshToken(refreshToken);
            return new AuthResponseDto(member.getAuth());
        }

        // IF NOT EXISTS AUTH ENTITY, SAVE AUTH ENTITY AND TOKEN
        Auth auth = this.authRepository.save(Auth.builder()
                .member(member)
                .tokenType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build());
        return new AuthResponseDto(auth);
    }

    /**
     * 회원 가입
     */
    @Transactional
    public void signup(MemberRequestDto requestDto) {
        // SAVE MEMBER ENTITIY
        requestDto.setRole(Role.ROLE_MEMBER);
        requestDto.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        this.memberRepository.save(requestDto.toEntity());
    }

    /**
     * Token 갱신
     */
    @Transactional
    public TokenPair refreshToken(String refreshToken) {
        // 체크: 리프레시 토큰이 유효한지 확인
        if (this.jwtTokenProvider.validateToken(refreshToken)) {
            Auth auth = this.authRepository.findByRefreshToken(refreshToken)
                    .orElseThrow(() -> new IllegalArgumentException("해당 REFRESH_TOKEN 을 찾을 수 없습니다. refresh_token = " + refreshToken));

            // 새 엑세스 토큰과 리프레시 토큰 생성
            String newAccessToken = this.jwtTokenProvider.generateAccessToken(
                    new UsernamePasswordAuthenticationToken(
                            new CustomMemberDetails(auth.getMember()), auth.getMember().getPassword()));
            String newRefreshToken = this.jwtTokenProvider.generateRefreshToken(
                    new UsernamePasswordAuthenticationToken(
                            new CustomMemberDetails(auth.getMember()), auth.getMember().getPassword()));

            // DB에 새 토큰 업데이트
            auth.updateAccessToken(newAccessToken);
            auth.updateRefreshToken(newRefreshToken);

            // 새 토큰 쌍 반환
            return new TokenPair(newAccessToken, newRefreshToken);
        } else {
            // 리프레시 토큰 만료 등의 이유로 새 토큰 생성 실패
            throw new SecurityException("리프레시 토큰이 유효하지 않습니다.");
        }
    }


    /**
     * 로그아웃
     */
    @Transactional
    public void logout(String accessToken) {
        // CHECK IF ACCESS_TOKEN EXPIRATION AVAILABLE, THEN DELETE AUTH ENTITY
        if (this.jwtTokenProvider.validateToken(accessToken)) {
            Auth auth = this.authRepository.findByAccessToken(accessToken).orElseThrow(
                    () -> new IllegalArgumentException("해당 ACCESS_TOKEN 을 찾을 수 없습니다. access_token = " + accessToken));
            this.authRepository.delete(auth);
        }
    }
}
