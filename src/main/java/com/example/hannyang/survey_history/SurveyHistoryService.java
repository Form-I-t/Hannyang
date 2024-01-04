package com.example.hannyang.survey_history;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SurveyHistoryService {

    @Autowired
    private SurveyHistoryRepository surveyHistoryRepository;

    // 설문조사 참여 내역 저장
    public void saveSurveyHistory(SurveyHistory surveyHistory) {
        surveyHistoryRepository.save(surveyHistory);
    }

    // 설문조사 참여 내역 삭제 설문조사 참여 내역 ID로
    public void deleteSurveyHistory(Long surveyHistoryId) {
        surveyHistoryRepository.deleteById(surveyHistoryId);
    }

    // 설문조사 참여 내역 삭제 설문조사 ID로


    // 설문조사 참여 내역 조회
    public SurveyHistory getSurveyHistory(Long surveyHistoryId) {
        return surveyHistoryRepository.findById(surveyHistoryId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid survey history ID: " + surveyHistoryId));
    }

    // 회원별 설문조사 참여 내역 조회
    public List<SurveyHistory> getSurveyHistoriesByMemberId(Long memberId) {
        return surveyHistoryRepository.findByMemberMemberId(memberId);
    }


}
