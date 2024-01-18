package com.example.hannyang.member.dtos;

import com.example.hannyang.member.Member;
import com.example.hannyang.member.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {
    private Role role;
    private String email;
    private String nickname;
    private String password;

    public Member toEntity() {
        return Member.builder()
                .role(this.role)
                .email(this.email)
                .nickname(this.nickname)
                .password(this.password)
                .build();
    }
}
