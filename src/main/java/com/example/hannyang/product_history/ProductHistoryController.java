package com.example.hannyang.product_history;

import com.example.hannyang.product_history.dtos.ProductHistoryRequestDto;
import com.example.hannyang.product_history.dtos.ProductHistoryResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
                    schema = @Schema(implementation = ProductHistoryResponseDto.class)))
    @GetMapping("/{memberId}")
    public List<ProductHistoryResponseDto> findByMemberId(@PathVariable Long memberId) {
        List<ProductHistory> productHistories = productHistoryRepository.findByMemberMemberId(memberId);
        return productHistories.stream()
                .map(ph -> new ProductHistoryResponseDto(
                        ph.getMember().getMemberId(),
                        ph.getProductName(),
                        ph.getUsedPoints(),
                        ph.getIsGiven(),
                        ph.getPurchaseDate()))
                .collect(Collectors.toList());
    }

    @Operation(summary = "상품 구매 내역 저장 및 포인트 차감",
            description = "상품 구매 내역을 저장하고 해당 회원의 포인트를 차감합니다.",
            tags = {"product-history"})
    @ApiResponse(responseCode = "200", description = "저장 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductHistoryRequestDto.class)))
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @PostMapping
    public void saveProductHistory(@RequestBody ProductHistoryRequestDto productHistoryRequestDTO) {
        productHistoryService.saveProductHistory(productHistoryRequestDTO);
    }

    // 상품 주문 내역 상품 지급 상태 변경
    @Operation(summary = "상품 지급 상태 변경",
            description = "상품 주문 내역의 상품 지급 상태를 변경합니다.",
            tags = {"product-history"})
    @ApiResponse(responseCode = "200", description = "변경 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @PostMapping("/change-status")
    public void changeStatus(Long productHistoryId, Boolean status) {
        productHistoryService.changeStatus(productHistoryId, status);
    }

    // 상품 전체 주문 내역 조회
    @Operation(summary = "상품 전체 주문 내역 조회",
            description = "상품 전체 주문 내역을 조회합니다.",
            tags = {"product-history"})
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductHistoryResponseDto.class)))
    @GetMapping("/all")
    public List<ProductHistoryResponseDto> findAll() {
        return productHistoryService.findAll();
    }
}
