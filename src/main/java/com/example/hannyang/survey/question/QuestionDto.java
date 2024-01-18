package com.example.hannyang.survey.question;

import com.example.hannyang.survey.choice.ChoiceDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuestionDto {
    private String content;
    private QuestionType type; // ENUM: MULTIPLE_CHOICE, CHECKBOX, DROPDOWN 등
    private List<ChoiceDto> choices;
}
