package com.example.hannyang.survey_request;

import com.example.hannyang.survey_request.dtos.Step1Dto;
import com.example.hannyang.survey_request.dtos.Step2Dto;
import com.example.hannyang.survey_request.dtos.Step3Dto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SurveyRequestService {
    private final SurveyRequestRepository repository;

    public SurveyRequestService(SurveyRequestRepository surveyRequestRepository) {
        this.repository = surveyRequestRepository;
    }

    // 첫 번째 단계에서 SurveyRequest 객체 생성 및 저장
    public SurveyRequest createSurveyRequest(Step1Dto step1Data) {
        SurveyRequest surveyRequest = new SurveyRequest();
        surveyRequest.setGoogleFormLink(step1Data.getGoogleFormLink());
        return repository.save(surveyRequest);
    }

    // 두 번째 단계에서 기존 SurveyRequest 찾아 업데이트
    public SurveyRequest updateSurveyRequestStep2(Long id, Step2Dto step2Data) {
        SurveyRequest surveyRequest = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SurveyRequest not found."));
        surveyRequest.setParticipantCount(step2Data.getParticipantCount());
        surveyRequest.setRewardPoints(step2Data.getRewardPoints());
        surveyRequest.setDeadline(step2Data.getDeadline());
        surveyRequest.setCreatedAt(step2Data.getCreatedAt());
        surveyRequest.setPrice(step2Data.getPrice());
        return repository.save(surveyRequest);
    }

    // 세 번째 단계에서 기존 SurveyRequest 찾아 업데이트
    public SurveyRequest updateSurveyRequestStep3(Long id, Step3Dto step3Data) {
        SurveyRequest surveyRequest = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SurveyRequest not found."));
        surveyRequest.setAccountHolderName(step3Data.getAccountHolderName());
        surveyRequest.setAccount(step3Data.getAccount());
        return repository.save(surveyRequest);
    }


}
