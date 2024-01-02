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

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime participationDate;
}
