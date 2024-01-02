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
        Member member = this.memberRepository.findByEmail(requestDto.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. username = " + requestDto.getUsername()));
        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다. username = " + requestDto.getUsername());
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
    public String refreshToken(String refreshToken) {
        // CHECK IF REFRESH_TOKEN EXPIRATION AVAILABLE, UPDAT ACCESS_TOKEN AND RETURN
        if (this.jwtTokenProvider.validateToken(refreshToken)) {
            Auth auth = this.authRepository.findByRefreshToken(refreshToken).orElseThrow(
                    () -> new IllegalArgumentException("해당 REFRESH_TOKEN 을 찾을 수 없습니다. refresh_token = " + refreshToken));

            String newAccessToken = this.jwtTokenProvider.generateAccessToken(
                    new UsernamePasswordAuthenticationToken(
                            new CustomMemberDetails(auth.getMember()), auth.getMember().getPassword()));
            auth.updateAccessToken(newAccessToken);
            return newAccessToken;
        }
        // IF NOT AVAILABLE REFRESH_TOKEN EXPIRATION, REGENERATE ACCESS_TOKEN AND REFRESH_TOKEN
        // IN THIS CASE, USER HAVE TO LOGIN AGAIN, SO REGENERATE IS NOT APPROPRIATE
        return null;
    }
    
}
