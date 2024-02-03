package com.example.hannyang.product_history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductHistoryRepository extends JpaRepository<ProductHistory, Long> {
    // 회원별 상품 구매 내역을 가져옵니다.
    List<ProductHistory> findByMemberId(Long memberId);
}
