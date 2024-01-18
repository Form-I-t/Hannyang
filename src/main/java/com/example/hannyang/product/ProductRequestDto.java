package com.example.hannyang.product;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProductRequestDto {
    private String name;
    private ProductType type; // Enum 타입
    private Integer price;
    private Integer quantity;
    private Boolean isSoldOut;
    private String description;
    private MultipartFile image; // 이미지 파일

    public Product toProduct() {
        Product product = new Product();
        product.setName(this.name);
        product.setType(this.type);
        product.setPrice(this.price);
        product.setQuantity(this.quantity);
        product.setIsSoldOut(this.isSoldOut);
        product.setDescription(this.description);
        // imageUrl은 S3 업로드 후 설정됩니다.
        return product;
    }

}
