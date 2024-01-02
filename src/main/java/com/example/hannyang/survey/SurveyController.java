package com.example.hannyang.survey;

import com.example.hannyang.survey.dtos.SurveyCrawlingDto;
import com.example.hannyang.survey.dtos.SurveyRequestDto;
import com.example.hannyang.survey.dtos.SurveyResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("survey")
@Tag(name = "survey", description = "설문조사 API")
public class SurveyController {
    private final SurveyService surveyService;

    @Autowired
    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @Operation(summary = "설문조사 등록", description = "새로운 설문조사를 등록합니다.", tags = {"survey"})
    @ApiResponse(responseCode = "200", description = "성공적으로 등록됨",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Survey.class)))
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "500", description = "서버 오류")
    @PostMapping
    public ResponseEntity<Survey> createSurvey(@RequestBody SurveyCrawlingDto crawlingDto,
                                               @RequestBody SurveyRequestDto requestDto) {
        Survey newSurvey = surveyService.createSurvey(crawlingDto, requestDto);
        return ResponseEntity.ok(newSurvey);
    }

    @Operation(summary = "설문조사 삭제", description = "특정 설문조사를 삭제합니다.", tags = {"survey"})
    @ApiResponse(responseCode = "200", description = "성공적으로 삭제됨")
    @ApiResponse(responseCode = "404", description = "설문조사를 찾을 수 없음")
    @ApiResponse(responseCode = "500", description = "서버 오류")
    @DeleteMapping("/{surveyId}")
    public ResponseEntity<Void> deleteSurvey(@PathVariable Long surveyId) {
        surveyService.deleteSurvey(surveyId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "최신 등록 순 설문 목록 조회", description = "최신 등록 순으로 설문 목록을 조회합니다.", tags = {"survey"})
    @ApiResponse(responseCode = "200", description = "성공적으로 조회됨")
    @ApiResponse(responseCode = "500", description = "서버 오류")
    @GetMapping("/latest")
    public ResponseEntity<List<Survey>> getSurveysOrderByCreatedAtDesc() {
        return ResponseEntity.ok(surveyService.getSurveysOrderByCreatedAtDesc());
    }

    @Operation(summary = "참여율 높은 순 설문 목록 조회", description = "참여율이 높은 순으로 설문 목록을 조회합니다.", tags = {"survey"})
    @ApiResponse(responseCode = "200", description = "성공적으로 조회됨")
    @ApiResponse(responseCode = "500", description = "서버 오류")
    @GetMapping("/popular")
    public ResponseEntity<List<Survey>> getSurveysOrderByParticipantCountDesc() {
        return ResponseEntity.ok(surveyService.getSurveysOrderByParticipantCountDesc());
    }

    @Operation(summary = "페이지별 최신 등록 순 설문 목록 조회", description = "페이지별로 최신 등록 순으로 설문 목록을 조회합니다.", tags = {"survey"})
    @ApiResponse(responseCode = "200", description = "성공적으로 조회됨")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "500", description = "서버 오류")
    @GetMapping("/paged")
    public ResponseEntity<Page<Survey>> getSurveysOrderByCreatedAtDescPaged(Pageable pageable) {
        return ResponseEntity.ok(surveyService.getSurveysOrderByCreatedAtDesc(pageable));
    }

    // 포인트별 설문조사 목록 조회
    @Operation(summary = "포인트별 설문조사 목록 조회",
            description = "지정된 포인트에 해당하는 설문조사 목록을 조회합니다.",
            tags = {"survey"})
    @ApiResponse(responseCode = "200", description = "성공적으로 조회됨")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "500", description = "서버 오류")
    @GetMapping("/by-points")
    public ResponseEntity<List<SurveyResponseDto>> getSurveysByPoints(@RequestParam Integer rewardPoints) {
        List<SurveyResponseDto> surveys = surveyService.getSurveysByPoints(rewardPoints);
        return ResponseEntity.ok(surveys);
    }

    // 마감 기한별 설문조사 목록 조회
    @Operation(summary = "마감 기한별 설문조사 목록 조회",
            description = "지정된 마감 기한에 해당하는 설문조사 목록을 조회합니다.",
            tags = {"survey"})
    @ApiResponse(responseCode = "200", description = "성공적으로 조회됨")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "500", description = "서버 오류")
    @GetMapping("/by-deadline")
    public ResponseEntity<List<SurveyResponseDto>> getSurveysByDeadline(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime deadline) {
        List<SurveyResponseDto> surveys = surveyService.getSurveysByDeadline(deadline);
        return ResponseEntity.ok(surveys);
    }

    @Operation(summary = "가성비가 높은 순으로 설문 목록 조회",
            description = "가성비 (지급 포인트/문항 수)가 높은 순으로 설문 목록을 조회합니다.",
            tags = {"survey"})
    @ApiResponse(responseCode = "200", description = "성공적으로 조회됨")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "500", description = "서버 오류")
    @GetMapping("/value-for-money")
    public ResponseEntity<List<Survey>> getSurveysOrderByValueForMoney() {
        List<Survey> surveys = surveyService.getSurveysOrderByValueForMoney();
        return ResponseEntity.ok(surveys);
    }
}
