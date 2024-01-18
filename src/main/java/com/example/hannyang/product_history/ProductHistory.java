package com.example.hannyang.product_history;

import com.example.hannyang.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ProductHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String productName; // 상품 이름

    private Integer usedPoints; // 상품 구매시 사용 포인트

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime purchaseDate; // 상품 구매 날짜
}
