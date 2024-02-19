package com.example.hannyang.member.dtos;

import com.example.hannyang.member.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberResponseDto {
    private Long id;
    private String role;
    private String nickname;
    private String email;
    private Integer point;
    private String profileImageUrl;

    public MemberResponseDto(Member entity) {
        this.id = entity.getMemberId();
        this.nickname = entity.getNickname();
        this.email = entity.getEmail();
        this.point = entity.getPoints();
        this.role = entity.getRole().name();
        this.profileImageUrl = entity.getProfileImageUrl();
    }
}
