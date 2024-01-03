package com.example.hannyang.member;

import com.example.hannyang.member.dtos.MemberRequestDto;
import com.example.hannyang.member.dtos.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * User 조회
     */
    @Transactional
    public MemberResponseDto findById(Long id) {
        Member member = this.memberRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. user_id = " + id));
        return new MemberResponseDto(member);
    }

    /**
     * User 수정
     */
    @Transactional
    public void update(Long id, MemberRequestDto requestDto) {
        Member member = this.memberRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. user_id = " + id));
        member.update(requestDto);
    }

    /**
     * User 삭제
     */
    @Transactional
    public void delete(Long id) {
        Member member = this.memberRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. user_id = " + id));
        this.memberRepository.delete(member);
    }

    /**
     * 비밀번호 변경
     */
    @Transactional
    public void changePassword(Long id, String password) {
        Member member = this.memberRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. user_id = " + id));
        member.changePassword(this.passwordEncoder.encode(password));
    }
}
