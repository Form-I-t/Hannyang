package com.example.hannyang.survey.dtos;

import lombok.Getter;

@Getter
public class SurveyCrawlingDto {
    private String surveyName; // 설문조사 이름
    private String requesterOrganization; // 요청자 소속
    private Integer questionCount; // 질문 개수
    private String restrictions; // 제한 사항

    // 기본 생성자 및 모든 필드를 포함하는 생성자 (선택적)
    public SurveyCrawlingDto() {
    }

    public SurveyCrawlingDto(String surveyName, String requesterOrganization, Integer questionCount, String restirctions) {
        this.surveyName = surveyName;
        this.requesterOrganization = requesterOrganization;
        this.questionCount = questionCount;
        this.restrictions = restirctions;
    }

    // Lombok 라이브러리가 게터와 세터 메소드를 자동으로 생성
}
