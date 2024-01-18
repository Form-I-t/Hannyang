package com.example.hannyang.survey.dtos;

import com.example.hannyang.survey.question.QuestionDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SurveyCrawlingDto {
    private String surveyName; // 설문조사 이름
    private String requesterOrganization; // 요청자 소속
    private Integer questionCount; // 질문 개수
    private String restrictions; // 제한 사항
    private List<QuestionDto> questions; // 질문 리스트


    // 기본 생성자 및 모든 필드를 포함하는 생성자 (선택적)
    public SurveyCrawlingDto() {
    }

    public SurveyCrawlingDto(String surveyName, String requesterOrganization, Integer questionCount, String restirictions, List<QuestionDto> questions) {
        this.surveyName = surveyName;
        this.requesterOrganization = requesterOrganization;
        this.questionCount = questionCount;
        this.restrictions = restirictions;
        this.questions = questions;
    }

}
