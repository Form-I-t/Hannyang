package com.example.hannyang.member;

import com.example.hannyang.member.dtos.MemberRequestDto;
import com.example.hannyang.member.dtos.MemberResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    /**
     * 멤버의 포인트 증가
     */
    // 멤버의 포인트 증가
    @Transactional
    public void addPoints(Long memberId, Integer pointsToAdd) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
        member.addPoints(pointsToAdd);
        memberRepository.save(member);
    }

    /**
     * 멤버의 포인트 감소
     */
    // 멤버의 포인트 감소
    @Transactional
    public void subtractPoints(Long memberId, Integer pointsToSubtract) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
        member.usePoints(pointsToSubtract);
        memberRepository.save(member);
    }

    // 닉네임 중복 확인
    public Optional<Member> findByNickname(String nickname) {
        return memberRepository.findByNickname(nickname);
    }

    // 이메일 중복 확인
    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
}
