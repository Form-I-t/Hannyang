package com.example.hannyang.survey;

import com.example.hannyang.survey.dtos.SurveyCrawlingDto;
import com.example.hannyang.survey.dtos.SurveyRequestDto;
import com.example.hannyang.survey.dtos.SurveyResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SurveyService {

    @Autowired
    private SurveyRepository surveyRepository;

    // 모든 설문조사 목록 조회
    public List<Survey> getAllSurveys() {
        return surveyRepository.findAll();
    }

    // 특정 설문조사 삭제 (설문 강제 종료) ++ 환불 리스트에 추가 하는 과정 넣어줘야함
    public void deleteSurvey(Long surveyId) {
        Optional<Survey> survey = surveyRepository.findById(surveyId);
        if (survey.isPresent()) {
            surveyRepository.delete(survey.get());
        } else {
            throw new IllegalArgumentException("Invalid survey ID: " + surveyId);
        }
    }

    // 설문조사 현재 참여인원 업데이트
    @Transactional
    public void addParticipation(Long surveyId) {
        boolean updated = false;
        while (!updated) {
            try {
                Survey survey = surveyRepository.findById(surveyId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid survey ID: " + surveyId));

                survey.setCurrentParticipants(survey.getCurrentParticipants() + 1);
                surveyRepository.save(survey);
                updated = true;
            } catch (ObjectOptimisticLockingFailureException e) {
                // 충돌 발생 시, 재시도 또는 로그 기록, 예외 처리 등을 수행합니다.
            }
        }
    }

    // 설문조사 등록 메서드

    public List<Survey> getSurveysOrderByCreatedAtDesc() {
        return surveyRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Survey> getSurveysOrderByParticipantCountDesc() {
        return surveyRepository.findAllByOrderByParticipantCountDesc();
    }

    public Page<Survey> getSurveysOrderByCreatedAtDesc(Pageable pageable) {
        return surveyRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    // 설문조사 지급 포인트별 조회
    public List<SurveyResponseDto> getSurveysByPoints(Integer rewardPoints) {
        List<Survey> surveys = surveyRepository.findByRewardPoints(rewardPoints);
        return surveys.stream()
                .map(survey -> new SurveyResponseDto(survey.getDeadline(), survey.getRewardPoints(), survey.getCurrentParticipants()))
                .collect(Collectors.toList());
    }

    // 설문조사 마감 기한별 조회
    public List<SurveyResponseDto> getSurveysByDeadline(LocalDateTime deadline) {
        List<Survey> surveys = surveyRepository.findByDeadline(deadline);
        return surveys.stream()
                .map(survey -> new SurveyResponseDto(survey.getDeadline(), survey.getRewardPoints(), survey.getCurrentParticipants()))
                .collect(Collectors.toList());
    }

    public Survey createSurvey(SurveyCrawlingDto crawlingDto, SurveyRequestDto requestDto) {
        Survey survey = new Survey();
        survey.setSurveyName(requestDto.getSurveyName());
        survey.setGoogleFormLink(requestDto.getGoogleFormLink());
        survey.setRewardPoints(requestDto.getRewardPoints());
        survey.setParticipantCount(requestDto.getParticipantCount());
        survey.setCurrentParticipants(0);
        survey.setDeadline(requestDto.getDeadline());
        survey.setAccount(requestDto.getAccount());
        survey.setRequesterOrganization(crawlingDto.getRequesterOrganization());
        survey.setQuestionCount(crawlingDto.getQuestionCount());
        survey.setRestrictions(crawlingDto.getRestrictions());
        return surveyRepository.save(survey);
    }

    // 가성비 높은 순으로 설문 목록 조회
    public List<Survey> getSurveysOrderByValueForMoney() {
        return surveyRepository.findAllOrderByValueForMoneyDesc();
    }
}
