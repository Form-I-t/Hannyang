package com.example.hannyang.survey.question;

import com.example.hannyang.survey.Survey;
import com.example.hannyang.survey.choice.Choice;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    private String content; // 문제 내용

    @Enumerated(EnumType.STRING)
    private QuestionType type; // 문제 유형 (예: "Checkbox", "Dropdown" 등)

    @Column(nullable = false)
    private Integer questionNumber; // 설문조사 내에서의 문제 번호

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Choice> choices = new ArrayList<>();

}

