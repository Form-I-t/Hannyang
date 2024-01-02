package com.example.hannyang.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // 특정 카테고리의 상품을 조회하는 메서드
    List<Product> findByType(ProductType type);
}
