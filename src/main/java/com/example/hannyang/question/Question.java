package com.example.hannyang.question;

import com.example.hannyang.survey.Survey;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content; // 공통적으로 가지는 질문 내용

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey; // 소속된 설문조사
    // 공통적인 메소드 및 속성
}
