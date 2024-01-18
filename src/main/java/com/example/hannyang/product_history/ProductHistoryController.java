package com.example.hannyang.product_history;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product-history")
@Tag(name = "product-history", description = "상품구매내역 API")
public class ProductHistoryController {
    private final ProductHistoryService productHistoryService;
    private final ProductHistoryRepository productHistoryRepository;

    @Operation(summary = "회원별 상품 구매 내역 조회",
            description = "특정 회원의 상품 구매 내역을 조회합니다.",
            tags = {"product-history"})
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductHistory.class)))
    @GetMapping("/{memberId}")
    public <List> ProductHistory findByMemberMemberId(Long memberId) {
        return productHistoryRepository.findByMemberMemberId(memberId);
    }

    @Operation(summary = "상품 구매 내역 저장 및 포인트 차감",
            description = "상품 구매 내역을 저장하고 해당 회원의 포인트를 차감합니다.",
            tags = {"product-history"})
    @ApiResponse(responseCode = "200", description = "저장 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductHistory.class)))
    @PostMapping
    public void saveProductHistory(ProductHistory productHistory, Integer usedPoints, Long memberId) {
        productHistoryService.saveProductHistory(productHistory, usedPoints, memberId);
    }
}
