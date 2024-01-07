package com.example.hannyang.survey_history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyHistoryRepository extends JpaRepository<SurveyHistory, Long> {
    // 회원별 설문조사 참여 내역을 가져옵니다.
    List<SurveyHistory> findByMemberMemberId(Long memberId);

    // 해당 선문조사 번호의 설문조사 참여 내역을 가져옵니다.
    List<SurveyHistory> findBySurveySurveyNumber(Long surveyNumber);

    // 해당 설문조사 번호의 설문조사 참여 내역을 삭제합니다.
    void deleteBySurveySurveyNumber(Long surveyNumber);


}
