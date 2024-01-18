package com.example.hannyang.survey.choice;

import com.example.hannyang.survey.question.Question;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Choice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long choiceId;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    private String content; // 선택지 내용

    @Column(nullable = false)
    private Integer choiceNumber; // 문제 내에서의 선택지 번호
}
