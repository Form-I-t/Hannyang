package com.example.hannyang.member;

import com.example.hannyang.member.auth.Auth;
import com.example.hannyang.member.dtos.MemberRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    private String name;

    @Column(length = 15)
    private String contact;

    private Integer points;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "member", cascade = CascadeType.REMOVE)
    private Auth auth;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public Member(String email, String password, String name, String contact, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.contact = contact;
        this.role = role;
    }

    // 포인트 적립 메서드
    public void earnPoints(Integer points) {
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
        this.name = requestDto.getName();
        this.contact = requestDto.getContact();
    }

    public void setPoints(int i) {
    }
}

