package com.example.hannyang.product_history;

import com.example.hannyang.member.Member;
import com.example.hannyang.member.MemberRepository;
import com.example.hannyang.member.MemberService;
import com.example.hannyang.point.Point;
import com.example.hannyang.point.PointService;
import com.example.hannyang.point.PointType;
import com.example.hannyang.product_history.dtos.ProductHistoryRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductHistoryService {

    private final ProductHistoryRepository productHistoryRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final PointService pointService;

    // 상품 구매 내역 저장 + 포인트 차감
    public void saveProductHistory(ProductHistoryRequestDto productHistoryRequestDTO) {
        Member member = memberRepository.findById(productHistoryRequestDTO.getMemberId()).orElseThrow(() -> new RuntimeException("Member not found"));

        // 상품 구매 내역 저장
        ProductHistory productHistory = new ProductHistory();
        productHistory.setMember(member);
        productHistory.setProductName(productHistoryRequestDTO.getProductName());
        productHistory.setUsedPoints(productHistoryRequestDTO.getUsedPoints());
        productHistory.setIsGiven(productHistoryRequestDTO.getIsGiven());
        productHistory.setPurchaseDate(LocalDateTime.now()); // 현재 시간 설정
        productHistoryRepository.save(productHistory);

        // 포인트 차감 로직
        Point point = new Point();
        point.setMember(member);
        point.setPoints(-productHistoryRequestDTO.getUsedPoints());
        point.setType(PointType.DEDUCT);
        point.setTransactionDate(LocalDateTime.now());
        pointService.createPoint(point);

        // 회원 포인트 차감
        memberService.subtractPoints(productHistoryRequestDTO.getMemberId(), productHistoryRequestDTO.getUsedPoints());
    }

    // 상품 주문 내역 상품 지급 상태 변경
    public void changeStatus(Long productHistoryId, Boolean status) {
        ProductHistory productHistory = productHistoryRepository.findById(productHistoryId).orElseThrow();
        productHistory.setIsGiven(status);
        productHistoryRepository.save(productHistory);
    }
}
