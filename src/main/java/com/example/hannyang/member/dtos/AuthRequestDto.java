package com.example.hannyang.member.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequestDto {
    private String email;  // 이메일을 사용자 아이디로 사용
    private String password;
}
