package com.example.hannyang.survey.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SurveyRequestDto {

    private String surveyName; // 설문조사 이름
    private String googleFormLink; // 구글 폼 주소
    private Integer rewardPoints; // 적립 포인트
    private Integer participantCount; // 필요한 참여자 수
    private LocalDateTime deadline; // 마감 기한
    private String account; // 계좌 정보

    // 기본 생성자 및 모든 필드를 포함하는 생성자
    public SurveyRequestDto() {
    }

    public SurveyRequestDto(String surveyName, String googleFormLink, Integer rewardPoints, Integer participantCount, LocalDateTime deadline, String account) {
        this.surveyName = surveyName;
        this.googleFormLink = googleFormLink;
        this.rewardPoints = rewardPoints;
        this.participantCount = participantCount;
        this.deadline = deadline;
        this.account = account;
    }
}
