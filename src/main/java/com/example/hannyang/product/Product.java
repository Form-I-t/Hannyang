package com.example.hannyang.product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductType type;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer quantity;

    private String imageUrl; // Amazon S3 URL

    @Column(nullable = false)
    private Boolean isSoldOut;

    // 상품 상세 설명
    @Column(nullable = false, length = 1000)
    private String description;


    // 상품의 재고를 감소시키는 메서드
    public void decreaseQuantity(Integer quantity) {
        if (this.quantity - quantity < 0) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
        this.quantity -= quantity;
    }

    // 상품의 재고를 증가시키는 메서드
    public void increaseQuantity(Integer quantity) {
        this.quantity += quantity;
    }

    // 상품의 판매 여부를 변경하는 메서드
    public void changeSoldOutStatus(Boolean isSoldOut) {
        this.isSoldOut = isSoldOut;
    }


}
