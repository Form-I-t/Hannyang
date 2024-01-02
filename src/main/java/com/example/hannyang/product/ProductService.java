package com.example.hannyang.product;

import com.example.hannyang.member.Member;
import com.example.hannyang.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    MemberRepository memberRepository;

    // 상품 등록
    @Transactional
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    // 전체 리스트 조회
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // 상품 타입별 리스트 조회
    public List<Product> getProductsByCategory(ProductType type) {
        return productRepository.findByType(type);
    }

    // 상품 구매
    @Transactional
    public void purchaseProduct(Long memberId, Long productId, int quantity) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));

        // 총 구매 비용 계산
        int totalCost = product.getPrice() * quantity;

        if (member.getPoints() >= totalCost && product.getQuantity() >= quantity) {
            member.setPoints(member.getPoints() - totalCost); // 포인트 차감
            memberRepository.save(member);

            product.setQuantity(product.getQuantity() - quantity); // 상품 수량 감소
            productRepository.save(product);

            // 구매 로직 추가 (예: 구매 내역 저장 등)
        } else {
            throw new IllegalArgumentException("Insufficient points or product quantity");
        }
    }
}

