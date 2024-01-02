package com.example.hannyang.survey_request;

import com.example.hannyang.survey_request.dtos.Step1Dto;
import com.example.hannyang.survey_request.dtos.Step2Dto;
import com.example.hannyang.survey_request.dtos.Step3Dto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("survey-requests")
@Tag(name = "survey_requests", description = "설문조사 요청 API")
public class SurveyRequestController {
    private final SurveyRequestService service;

    @Autowired
    public SurveyRequestController(SurveyRequestService service) {
        this.service = service;
    }

    // 첫 번째 단계 데이터를 받아 새 SurveyRequest 객체를 생성하고 저장
    @Operation(summary = "1단계 설문조사 정보 저장",
            description = "구글 폼 링크 정보를 저장합니다.",
            tags = {"survey-requests"})
    @ApiResponse(responseCode = "200", description = "성공적으로 저장됨",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SurveyRequest.class)))
    @PostMapping("/step1")
    public ResponseEntity<SurveyRequest> createSurveyRequest(@RequestBody Step1Dto step1Data) {
        SurveyRequest newSurveyRequest = service.createSurveyRequest(step1Data);
        return ResponseEntity.ok(newSurveyRequest);
    }

    // 두 번째 단계 데이터를 받아 기존 SurveyRequest 객체를 찾아 업데이트
    @Operation(summary = "2단계 설문조사 정보 저장",
            description = "희망 응답자 수, 부여 포인트, 게시 기간 정보를 저장합니다.",
            tags = {"survey-requests"})
    @ApiResponse(responseCode = "200", description = "성공적으로 저장됨")
    @PutMapping("/step2/{id}")
    public ResponseEntity<SurveyRequest> updateSurveyRequestStep2(@PathVariable Long id, @RequestBody Step2Dto step2Data) {
        SurveyRequest updatedSurveyRequest = service.updateSurveyRequestStep2(id, step2Data);
        return ResponseEntity.ok(updatedSurveyRequest);
    }

    // 세 번째 단계 데이터를 받아 기존 SurveyRequest 객체를 찾아 업데이트
    @Operation(summary = "3단계 설문조사 정보 저장",
            description = "예금주명, 환불 계좌 정보를 저장합니다.",
            tags = {"survey-requests"})
    @ApiResponse(responseCode = "200", description = "성공적으로 저장됨")
    @PutMapping("/step3/{id}")
    public ResponseEntity<SurveyRequest> updateSurveyRequestStep3(@PathVariable Long id, @RequestBody Step3Dto step3Data) {
        SurveyRequest updatedSurveyRequest = service.updateSurveyRequestStep3(id, step3Data);
        return ResponseEntity.ok(updatedSurveyRequest);
    }

}
