package com.example.hannyang.product_history.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductHistoryRequestDto {
    private Long memberId;
    private String productName;
    private Integer usedPoints;
    private Boolean isGiven;
}
