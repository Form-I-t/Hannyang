package com.example.hannyang.survey_request.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Step2Dto {
    private Integer participantCount;// 희망 응답자 수
    private Integer rewardPoints; // 부여 포인트
    private LocalDateTime deadline; // 게시 기간
}
