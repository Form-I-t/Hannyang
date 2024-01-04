package com.example.hannyang.survey_history;

import com.example.hannyang.member.Member;
import com.example.hannyang.survey.Survey;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class SurveyHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @Column(nullable = false)
    private String surveyName; // 설문조사 이름

    private Integer rewardPoints; // 부여 적립 포인트

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime participationDate;
}
