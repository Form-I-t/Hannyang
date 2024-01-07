package com.example.hannyang.point;

import com.example.hannyang.member.Member;
import com.example.hannyang.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PointService {

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private MemberRepository memberRepository;

    // 포인트 정보 저장
    public Point createPoint(Point point) {
        return pointRepository.save(point);
    }

    //포인트 내역 조회
    public List<Point> getPointsByMember(Long memberId) {
        Optional<Member> memberOpt = memberRepository.findById(memberId);
        if (memberOpt.isPresent()) {
            List<Point> points = pointRepository.findByMember(memberOpt.get());
            points.forEach(Point::updateExpirationStatus);
            return points;
        }
        return Collections.emptyList(); // null 대신 비어 있는 리스트 반환
    }

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    public void deleteExpiredPoints() {
        LocalDateTime now = LocalDateTime.now();
        List<Point> expiredPoints = pointRepository.findByExpirationDateBeforeAndIsExpired(now, false);

        for (Point point : expiredPoints) {
            Member member = point.getMember();
            member.usePoints(point.getPoints()); // 포인트 차감
            memberRepository.save(member); // 업데이트된 회원 정보 저장

            pointRepository.delete(point); // 포인트 삭제
        }
    }

}

