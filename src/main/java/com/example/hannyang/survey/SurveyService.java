package com.example.hannyang.survey;

import com.example.hannyang.survey.choice.Choice;
import com.example.hannyang.survey.choice.ChoiceDto;
import com.example.hannyang.survey.choice.ChoiceRepository;
import com.example.hannyang.survey.dtos.SurveyCrawlingDto;
import com.example.hannyang.survey.dtos.SurveyCreationDto;
import com.example.hannyang.survey.dtos.SurveyRequestDto;
import com.example.hannyang.survey.question.Question;
import com.example.hannyang.survey.question.QuestionDto;
import com.example.hannyang.survey.question.QuestionRepository;
import com.example.hannyang.survey.question.QuestionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SurveyService {

    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private ChoiceRepository choiceRepository;

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
    public List<Survey> getSurveysByPoints() {
        return surveyRepository.findAllByOrderByRewardPointsDesc();

    }

    // 설문조사 마감 기한별 조회
    public List<Survey> getSurveysByDeadline() {
        return surveyRepository.findAllByOrderByDeadlineAsc();

    }

    // 설문조사 등록 메서드
    @Transactional
    public Survey createSurvey(SurveyCreationDto creationDto) {
        Survey survey = new Survey();

        // SurveyRequestDto에서 정보 설정
        SurveyRequestDto requestDto = creationDto.getRequestDto();
        survey.setSurveyName(requestDto.getSurveyName());
        survey.setGoogleFormLink(requestDto.getGoogleFormLink());
        survey.setRewardPoints(requestDto.getRewardPoints());
        survey.setParticipantCount(requestDto.getParticipantCount());
        survey.setCurrentParticipants(0);
        survey.setDeadline(requestDto.getDeadline());
        survey.setAccount(requestDto.getAccount());

        // SurveyCrawlingDto에서 정보 설정
        SurveyCrawlingDto crawlingDto = creationDto.getCrawlingDto();
        survey.setRequesterOrganization(crawlingDto.getRequesterOrganization());
        survey.setQuestionCount(crawlingDto.getQuestionCount());
        survey.setRestrictions(crawlingDto.getRestrictions());

        for (QuestionDto questionDto : crawlingDto.getQuestions()) {
            Question question = new Question();
            question.setContent(questionDto.getContent());
            question.setType(QuestionType.valueOf(questionDto.getType().name()));

            if (question.getType() != QuestionType.SHORT_ANSWER) { // 주관식을 제외한 경우
                for (ChoiceDto choiceDto : questionDto.getChoices()) {
                    Choice choice = new Choice();
                    choice.setContent(choiceDto.getContent());
                    question.getChoices().add(choice);
                }
            }
            // 주관식의 경우, choices 리스트를 추가하지 않음

            survey.getQuestions().add(question);
        }

        return surveyRepository.save(survey);
    }


    // 가성비 높은 순으로 설문 목록 조회
    public List<Survey> getSurveysOrderByValueForMoney() {
        return surveyRepository.findAllOrderByValueForMoneyDesc();
    }
}
