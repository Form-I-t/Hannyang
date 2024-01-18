package com.example.hannyang.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // 특정 카테고리의 상품을 조회하는 메서드
    List<Product> findByType(ProductType type);

    // 특정 이름의 상품 조회하는 메서드
    Optional<Product> findByName(String name);
}
