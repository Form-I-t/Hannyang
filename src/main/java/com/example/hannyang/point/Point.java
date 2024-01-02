package com.example.hannyang.point;

import com.example.hannyang.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime transactionDate;

    // 포인트 양 (적립은 양수, 사용은 음수)
    private Integer points = 0;

    @Enumerated(EnumType.STRING)
    private PointType type;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // 적립 포인트에만 적용되는 필드
    private LocalDateTime expirationDate;
    private Boolean isExpired;

    // 만료 여부 갱신 메서드 (적립 포인트에만 적용) , 조회할때마다 만료 여부 갱신
    public void updateExpirationStatus() {
        if (this.type == PointType.EARN && expirationDate != null) {
            this.isExpired = LocalDateTime.now().isAfter(expirationDate);
        }
    }
}

