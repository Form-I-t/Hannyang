package com.example.hannyang.product_history;

import com.example.hannyang.member.MemberService;
import com.example.hannyang.point.Point;
import com.example.hannyang.point.PointService;
import com.example.hannyang.point.PointType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductHistoryService {

    private final ProductHistoryRepository productHistoryRepository;
    private final MemberService memberService;
    private final PointService pointService;

    // 상품 구매 내역 저장 + 포인트 차감
    public void saveProductHistory(ProductHistory productHistory, Integer usedPoints, Long memberId) {
        // 상품 구매 내역 저장
        productHistoryRepository.save(productHistory);

        // 포인트 차감 로직
        Point point = new Point();
        point.setMember(productHistory.getMember()); // Member 엔티티 설정
        point.setPoints(-usedPoints); // 차감되는 포인트이므로 음수
        point.setType(PointType.DEDUCT); // 포인트 사용 유형
        point.setTransactionDate(LocalDateTime.now()); // 거래 날짜
        point.setExpirationDate(null); // 만료 날짜 설정 (예시)
        pointService.createPoint(point); // PointService를 통해 Point 엔티티 저장

        // 회원 포인트 차감
        memberService.subtractPoints(memberId, usedPoints);
    }

    // 상품 주문 내역 상품 지급 상태 변경
    public void changeStatus(Long productHistoryId, Boolean status) {
        ProductHistory productHistory = productHistoryRepository.findById(productHistoryId).orElseThrow();
        productHistory.setIsGiven(status);
        productHistoryRepository.save(productHistory);
    }
}
