package com.example.hannyang.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomMemberDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        Member member = memberRepository.findByNickname(nickname).orElseThrow(
                () -> new UsernameNotFoundException("해당 유저가 존재하지 않습니다. name = " + nickname));
        return new CustomMemberDetails(member); // 위에서 생성한 CustomMemberDetails Class
    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("해당 유저가 존재하지 않습니다. email = " + email));
        return new CustomMemberDetails(member); // 위에서 생성한 CustomMemberDetails Class
    }

    public UserDetails loadUserBUserId(Long memberId) throws UsernameNotFoundException {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new UsernameNotFoundException("해당 유저가 존재하지 않습니다. memberId = " + memberId));
        return new CustomMemberDetails(member); // 위에서 생성한 CustomMemberDetails Class
    }
}
