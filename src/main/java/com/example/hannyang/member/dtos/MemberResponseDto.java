package com.example.hannyang.member.dtos;

import com.example.hannyang.member.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberResponseDto {
    private Long id;
    private String role;
    private String username;
    private String email;
    private String contact;
    private Integer point;

    public MemberResponseDto(Member entity) {
        this.id = entity.getMemberId();
        this.username = entity.getNickname();
        this.email = entity.getEmail();
        this.point = entity.getPoints();
        this.role = entity.getRole().name();
    }
}
