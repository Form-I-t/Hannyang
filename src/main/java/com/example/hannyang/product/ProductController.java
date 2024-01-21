package com.example.hannyang.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
@Tag(name = "product", description = "포인트 상품 API")
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "상품 초기 등록", description = "새로운 상품을 등록합니다.", tags = {"product"})
    @ApiResponse(responseCode = "200", description = "성공적으로 등록됨",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Product.class)))
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "500", description = "서버 오류")
    @PostMapping("/create-product")
    public ResponseEntity<Product> createProduct(@ModelAttribute ProductRequestDto productRequestDto) throws IOException {
        Product product = productRequestDto.toProduct();
        Product savedProduct = productService.createProductWithImage(product, productRequestDto.getImage());
        return ResponseEntity.ok(savedProduct);
    }

    // 상품명으로 상품 갯수 증가
    @Operation(summary = "상품 수량 증가", description = "상품명으로 상품의 수량을 증가시킵니다.", tags = {"product"})
    @ApiResponse(responseCode = "200", description = "성공적으로 수량 증가",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Product.class)))
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
    @ApiResponse(responseCode = "500", description = "서버 오류")
    @PostMapping("/increase-product-quantity")
    public ResponseEntity<?> increaseProductQuantityByName(
            @RequestParam String name,
            @RequestParam int quantity) {
        try {
            Product product = productService.increaseProductQuantityByName(name, quantity);
            return ResponseEntity.ok(product);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    // 전체 리스트 조회
    @Operation(summary = "전체 상품 조회", description = "전체 상품 리스트를 조회합니다.", tags = {"product"})
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Product.class)))
    @GetMapping("/all-products")
    public ResponseEntity<?> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // 상품 타입별 리스트 조회
    @Operation(summary = "상품 타입별 상품 조회", description = "상품 타입별 상품 리스트를 조회합니다.", tags = {"product"})
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Product.class)))
    @GetMapping("/products-by-category")
    public ResponseEntity<?> getProductsByCategory(@RequestParam ProductType type) {
        return ResponseEntity.ok(productService.getProductsByCategory(type));
    }
}
