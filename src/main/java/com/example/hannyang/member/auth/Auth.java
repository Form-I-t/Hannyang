package com.example.hannyang.member.auth;

import com.example.hannyang.member.BaseTime;
import com.example.hannyang.member.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class Auth extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tokenType;

    @Column(nullable = false)
    private String accessToken;

    @Column(nullable = false)
    private String refreshToken;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Auth(String tokenType, String accessToken, String refreshToken, Member member) {
        this.member = member;
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;

    }

    void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
