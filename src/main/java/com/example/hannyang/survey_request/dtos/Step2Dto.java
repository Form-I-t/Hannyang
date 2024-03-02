package com.example.hannyang.survey_request.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Step2Dto {
    private Integer participantCount;// 희망 응답자 수
    private Integer rewardPoints; // 부여 포인트
    private LocalDate deadline; // 게시 기간
    private LocalDate createdAt; // 생성일
    private Integer price; // 가격
}
