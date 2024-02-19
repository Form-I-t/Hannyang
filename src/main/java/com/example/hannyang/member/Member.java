package com.example.hannyang.member;

import com.example.hannyang.member.auth.Auth;
import com.example.hannyang.member.dtos.MemberRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String nickname;


    @Column(nullable = false)
    private int points = 0;


    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "member", cascade = CascadeType.REMOVE)
    private Auth auth;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    // 프로필 이미지 URL 변경 메서드
    @Setter
    private String profileImageUrl; // 프로필 사진 URL (Amazon S3)

    @Builder
    public Member(String email, String password, String nickname, Role role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // 포인트 적립 메서드
    public void addPoints(Integer points) {
        this.points += points;
    }

    // 포인트 사용 메서드
    public void usePoints(Integer points) {
        if (this.points - points < 0) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }
        this.points -= points;
    }

    // 회원 정보 수정 메서드
    public void update(MemberRequestDto requestDto) {
        this.email = requestDto.getEmail();
        this.password = requestDto.getPassword();
        this.nickname = requestDto.getNickname();
    }

    // 회원 프로필 사진 저장 메서드


    public void changePassword(String encode) {
        this.password = encode;
    }

    public void setPoints(int i) {
        this.points = i;
    }

}

