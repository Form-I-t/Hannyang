package com.example.hannyang.survey_history;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/survey-history")
@RequiredArgsConstructor
@Tag(name = "survey-history", description = "설문조사 참여내역 API")
public class SurveyHistoryController {

    private final SurveyHistoryService surveyHistoryService;

    // 설문조사 참여 내역 저장 + 포인트 부여 ++ 포인트 적립 내역 저장
    @Operation(summary = "설문조사 참여 내역 저장 + 포인트 부여", description = "새로운 설문조사 참여 내역을 등록합니다.", tags = {"survey-history"})
    @ApiResponse(responseCode = "200", description = "성공적으로 저장됨",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SurveyHistory.class)))
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "500", description = "서버 오류")
    @PostMapping
    public ResponseEntity<SurveyHistory> createSurveyHistory(@RequestBody SurveyHistory surveyHistory) {
        SurveyHistory savedSurveyHistory = surveyHistoryService.save(surveyHistory);
        return ResponseEntity.ok(savedSurveyHistory);
    }

    // 설문조사 참여 내역 삭제 (설문조사 참여 내역 ID로)
    @Operation(summary = "설문조사 참여 내역 삭제 (ID로)", description = "특정 설문조사 참여 내역을 삭제합니다.", tags = {"survey-history"})
    @ApiResponse(responseCode = "200", description = "성공적으로 삭제됨")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "404", description = "찾을 수 없음")
    @ApiResponse(responseCode = "500", description = "서버 오류")
    @DeleteMapping("/{surveyHistoryId}")
    public ResponseEntity<?> deleteSurveyHistory(@PathVariable Long surveyHistoryId) {
        surveyHistoryService.deleteSurveyHistory(surveyHistoryId);
        return ResponseEntity.ok().build();
    }

    // 설문조사 참여 내역 삭제 (설문조사 번호로)
    @Operation(summary = "설문조사 참여 내역 삭제 (설문조사 번호로)", description = "특정 설문조사 번호와 관련된 모든 설문조사 참여 내역을 삭제합니다.", tags = {"survey-history"})
    @ApiResponse(responseCode = "200", description = "성공적으로 삭제됨")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "404", description = "찾을 수 없음")
    @ApiResponse(responseCode = "500", description = "서버 오류")
    @DeleteMapping("/by-survey-number/{surveyNumber}")
    public ResponseEntity<?> deleteSurveyHistoryBySurveyNumber(@PathVariable Long surveyNumber) {
        surveyHistoryService.deleteSurveyHistoryBySurveyNumber(surveyNumber);
        return ResponseEntity.ok().build();
    }

    // 설문조사 참여 내역 조회
    @Operation(summary = "설문조사 참여 내역 조회 (ID로)", description = "특정 ID를 가진 설문조사 참여 내역을 조회합니다.", tags = {"survey-history"})
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SurveyHistory.class)))
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "404", description = "찾을 수 없음")
    @ApiResponse(responseCode = "500", description = "서버 오류")
    @GetMapping("/{surveyHistoryId}")
    public ResponseEntity<SurveyHistory> getSurveyHistory(@PathVariable Long surveyHistoryId) {
        SurveyHistory surveyHistory = surveyHistoryService.getSurveyHistory(surveyHistoryId);
        return ResponseEntity.ok(surveyHistory);
    }

    // 회원별 설문조사 참여 내역 조회
    @Operation(summary = "회원별 설문조사 참여 내역 조회", description = "특정 회원 ID를 가진 사용자의 모든 설문조사 참여 내역을 조회합니다.", tags = {"survey-history"})
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SurveyHistory[].class)))
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "404", description = "찾을 수 없음")
    @ApiResponse(responseCode = "500", description = "서버 오류")
    @GetMapping("/by-member/{memberId}")
    public ResponseEntity<List<SurveyHistory>> getSurveyHistoriesByMemberId(@PathVariable Long memberId) {
        List<SurveyHistory> surveyHistories = surveyHistoryService.getSurveyHistoriesByMemberId(memberId);
        return ResponseEntity.ok(surveyHistories);
    }

    // 설문조사 참여 내역 조회 (설문조사 번호로)
    @Operation(summary = "설문조사 참여 내역 조회 (설문조사 번호로)", description = "특정 설문조사 번호와 관련된 모든 설문조사 참여 내역을 조회합니다.", tags = {"survey-history"})
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SurveyHistory[].class)))
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "404", description = "찾을 수 없음")
    @ApiResponse(responseCode = "500", description = "서버 오류")
    @GetMapping("/by-survey-number/{surveyNumber}")
    public ResponseEntity<List<SurveyHistory>> getSurveyHistoriesBySurveyNumber(@PathVariable Long surveyNumber) {
        List<SurveyHistory> surveyHistories = surveyHistoryService.getSurveyHistoriesBySurveyNumber(surveyNumber);
        return ResponseEntity.ok(surveyHistories);
    }
}
