package com.example.hannyang.survey.dtos;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SurveyResponseDto {

    private final LocalDateTime deadline; // 마감 기한
    private final Integer rewardPoints; // 적립 포인트
    private final Integer currentParticipants; // 현재 참여자 수

    // 생성자
    public SurveyResponseDto(LocalDateTime deadline, Integer rewardPoints, Integer currentParticipants) {
        this.deadline = deadline;
        this.rewardPoints = rewardPoints;
        this.currentParticipants = currentParticipants;
    }

}

