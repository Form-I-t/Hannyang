package com.example.hannyang.member;

import com.example.hannyang.member.dtos.MemberProfileResponseDto;
import com.example.hannyang.member.dtos.MemberRequestDto;
import com.example.hannyang.member.dtos.MemberResponseDto;
import com.example.hannyang.product_history.ProductHistory;
import com.example.hannyang.product_history.ProductHistoryRepository;
import com.example.hannyang.s3.S3ClientService;
import com.example.hannyang.survey.Survey;
import com.example.hannyang.survey.SurveyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final SurveyRepository surveyRepository;
    private final ProductHistoryRepository productHistoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3ClientService s3ClientService;

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

    // 회원 프로필 사진 저장
    @Transactional
    public void saveProfileImage(Long memberId, MultipartFile profileImage) throws IOException {
        if (profileImage != null && !profileImage.isEmpty()) {
            // 파일을 임시로 저장할 경로를 생성합니다.
            Path tempFilePath = Files.createTempFile("profile-", ".tmp");

            // MultipartFile을 임시 파일로 복사합니다.
            Files.copy(profileImage.getInputStream(), tempFilePath, StandardCopyOption.REPLACE_EXISTING);

            // 파일을 S3에 업로드하고, 업로드된 파일의 URL을 가져옵니다.
            String fileName = generateFileName(profileImage);
            s3ClientService.uploadFile(fileName, tempFilePath);
            String profileImageUrl = s3ClientService.getFileUrl(fileName);

            // 임시 파일을 삭제합니다.
            Files.delete(tempFilePath);

            // Member 엔티티를 조회하고 profileImageUrl을 업데이트합니다.
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid member Id: " + memberId));
            member.setProfileImageUrl(profileImageUrl);
            memberRepository.save(member);
        }
    }

    // 회원 프로필 페이지 정보
    public MemberProfileResponseDto findProfileInfo(Long memberId) {
        // 회원 정보 조회
        // 회원 정보 조회
        MemberResponseDto memberInfo = memberRepository.findById(memberId)
                .map(member -> new MemberResponseDto(member))
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));


        // survey 데이터 조회
        List<Survey> surveys = surveyRepository.findByMemberMemberId(memberId);

        // productHistory 데이터 조회
        List<ProductHistory> productHistories = productHistoryRepository.findByMemberMemberId(memberId);

        // 조회된 데이터를 DTO에 담아 반환
        return new MemberProfileResponseDto(memberInfo, surveys, productHistories);
    }

    private String generateFileName(MultipartFile profileImage) {
        String fileName = UUID.randomUUID() + profileImage.getOriginalFilename();
        return "profile-images/" + fileName;
    }
}
