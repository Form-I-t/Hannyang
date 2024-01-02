package com.example.hannyang.member.auth;


import com.example.hannyang.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<Auth, Long> {
    boolean existsByMember(Member member);

    Optional<Auth> findByRefreshToken(String refreshToken);
}
