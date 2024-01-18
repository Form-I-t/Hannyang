package com.example.hannyang.member;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomMemberDetails implements UserDetails {
    private final Member member;

    public CustomMemberDetails(Member member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(member.getRole().name()));
    }

    // Member의 고유 ID, 이메일, 연락처 등 추가적인 정보 제공
    public Long getId() {
        return member.getMemberId();
    }

    public String getEmail() {
        return member.getEmail();
    }


    @Override
    public String getUsername() {
        return member.getNickname(); // Member의 고유 이름
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    ///////////////////////////////////////////////////////
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

