package com.example.hannyang.survey.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SurveyCreationDto {
    private SurveyRequestDto requestDto;
    private SurveyCrawlingDto crawlingDto;
}
