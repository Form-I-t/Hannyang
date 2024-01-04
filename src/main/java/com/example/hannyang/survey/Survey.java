package com.example.hannyang.survey;

import com.example.hannyang.member.Member;
import com.example.hannyang.question.Question;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long surveyNumber; // 설문조사 번호

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member; // 회원

    @Column(nullable = false)
    private String surveyName; // 설문조사 이름

    @Column(nullable = false)
    private String googleFormLink; // 구글 폼 주소

    private Integer rewardPoints; // 부여 적립 포인트

    private Integer participantCount; // 참여자 수


    private int currentParticipants = 0; // 현재까지 참여자 수
    private LocalDateTime deadline; // 마감 기한

    private String account; // 환불 계좌 정보

    private String requesterOrganization; // 요청자 소속
    private Integer questionCount; // 질문 개수
    private String restrictions; // 제한 사항
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 등록일

    @LastModifiedDate
    private LocalDateTime updatedAt; // 수정일

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    // 가성비를 계산하는 메소드
    @Transient
    public Double getValueForMoney() {
        if (questionCount != null && questionCount > 0) {
            return (double) rewardPoints / questionCount;
        }
        return 0.0;
    }
}
