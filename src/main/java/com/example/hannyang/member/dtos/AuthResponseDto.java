package com.example.hannyang.member.dtos;

import com.example.hannyang.member.auth.Auth;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {
    private Long memberId; // Member의 id를 위한 필드
    private String tokenType;
    private String accessToken;
    private String refreshToken;

    @Builder
    public AuthResponseDto(Auth entity) {
        this.memberId = entity.getMember().getMemberId();
        this.tokenType = entity.getTokenType();
        this.accessToken = entity.getAccessToken();
        this.refreshToken = entity.getRefreshToken();
    }
}
