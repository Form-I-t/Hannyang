package com.example.hannyang.survey_history;

import com.example.hannyang.member.MemberRepository;
import com.example.hannyang.point.Point;
import com.example.hannyang.point.PointService;
import com.example.hannyang.point.PointType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class SurveyHistoryService {

    @Autowired
    private SurveyHistoryRepository surveyHistoryRepository;
    @Autowired
    private PointService pointService;
    @Autowired
    private MemberRepository memberRepository; // 포인트 적립을 위해 회원 정보 가져옴

    // 설문조사 참여 내역 저장 + 포인트 적립
    @Transactional
    public SurveyHistory save(SurveyHistory surveyHistory) {
        SurveyHistory savedSurveyHistory = surveyHistoryRepository.save(surveyHistory);

        Point point = new Point();
        point.setMember(surveyHistory.getMember());
        point.setPoints(surveyHistory.getRewardPoints());
        point.setType(PointType.EARN);
        point.setTransactionDate(LocalDateTime.now());
        point.setExpirationDate(LocalDateTime.now().plusDays(30));
        pointService.createPoint(point);

        return savedSurveyHistory;
    }

    // 설문조사 참여 내역 삭제 설문조사 참여 내역 ID로
    public void deleteSurveyHistory(Long surveyHistoryId) {
        surveyHistoryRepository.deleteById(surveyHistoryId);
    }

    // 설문조사 참여 내역 삭제 설문조사 번호로
    public void deleteSurveyHistoryBySurveyNumber(Long surveyNumber) {
        surveyHistoryRepository.deleteBySurveySurveyNumber(surveyNumber);
    }

    // 설문조사 참여 내역 조회
    public SurveyHistory getSurveyHistory(Long surveyHistoryId) {
        return surveyHistoryRepository.findById(surveyHistoryId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid survey history ID: " + surveyHistoryId));
    }

    // 회원별 설문조사 참여 내역 조회
    public List<SurveyHistory> getSurveyHistoriesByMemberId(Long memberId) {
        return surveyHistoryRepository.findByMemberMemberId(memberId);
    }

    // 설문조사 참여 내역 조회 설문조사 번호로
    public List<SurveyHistory> getSurveyHistoriesBySurveyNumber(Long surveyNumber) {
        return surveyHistoryRepository.findBySurveySurveyNumber(surveyNumber);
    }
}
