package com.example.hannyang.survey_history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyHistoryRepository extends JpaRepository<SurveyHistory, Long> {
    // 회원별 설문조사 참여 내역을 가져옵니다.
    List<SurveyHistory> findByMemberMemberId(Long memberId);

    List<SurveyHistory> findBySurveySurveyNumber(Long surveyNumber);

}
