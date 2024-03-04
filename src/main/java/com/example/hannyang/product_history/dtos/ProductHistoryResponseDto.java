package com.example.hannyang.product_history.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductHistoryResponseDto {
    private final Long memberId;
    private final String productName;
    private final Integer usedPoints;
    private final Boolean isGiven;
    private final LocalDateTime purchaseDate;

    public ProductHistoryResponseDto(Long memberId, String productName, Integer usedPoints, Boolean isGiven, LocalDateTime purchaseDate) {
        this.memberId = memberId;
        this.productName = productName;
        this.usedPoints = usedPoints;
        this.isGiven = isGiven;
        this.purchaseDate = purchaseDate;
    }
}
