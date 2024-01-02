package com.example.hannyang.point;

import com.example.hannyang.member.Member;
import com.example.hannyang.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    //포인트 적립 저장
    public void earnPoints(Long memberId, int points, LocalDateTime expirationDate) {
        Optional<Member> memberOpt = memberRepository.findById(memberId);
        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();

            //적립 포인트를 회원의 포인트에 추가
            member.setPoints(member.getPoints() + points);

            Point point = new Point();
            point.setMember(memberOpt.get());
            point.setPoints(points);
            point.setType(PointType.EARN);
            point.setTransactionDate(LocalDateTime.now());
            point.setExpirationDate(expirationDate);
            pointRepository.save(point);
        }
    }

    // 포인트 차감 저장
    public void deductPoints(Long memberId, int points) {
        Optional<Member> memberOpt = memberRepository.findById(memberId);
        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();

            // 차감 포인트를 회원의 포인트에서 빼기
            // 차감 전 충분한 포인트가 있는지 확인 --> 상품 구매 쪽에서 추가
            member.setPoints(member.getPoints() - points);
            Point point = new Point();
            point.setMember(memberOpt.get());
            point.setPoints(points);
            point.setType(PointType.DEDUCT); // 차감을 나타내는 타입으로 변경
            point.setTransactionDate(LocalDateTime.now());
            // 차감 포인트에는 만료일자와 만료여부가 필요 없음.
            pointRepository.save(point);
        } else {
            throw new IllegalArgumentException("Invalid member ID: " + memberId);
        }
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
}

