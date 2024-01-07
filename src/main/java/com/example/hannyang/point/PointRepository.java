package com.example.hannyang.point;

import com.example.hannyang.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {
    List<Point> findByMember(Member member);
    
    List<Point> findByExpirationDateBeforeAndIsExpired(LocalDateTime expirationDate, Boolean isExpired);
}
