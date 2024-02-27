package com.example.hannyang.survey_request;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class SurveyRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String googleFormLink; // 구글 폼 주소

    // 2단계 정보
    private Integer participantCount;// 희망 응답자 수
    private Integer rewardPoints; // 부여 포인트
    private LocalDateTime deadline; // 게시 기간
    private LocalDateTime createdAt; // 생성일
    private Integer price; // 가격

    // 3단계 정보
    private String accountHolderName; // 예금주명
    private String account; // 환불 계좌

}

