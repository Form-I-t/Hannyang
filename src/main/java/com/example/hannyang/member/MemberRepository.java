package com.example.hannyang.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByName(String username);

    Optional<Member> findByEmail(String email);

    void deleteById(Long memberId);

    Optional<Member> findById(Long memberId);


}
