package com.example.hannyang.point;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/points")
@RequiredArgsConstructor
public class PointController {
    private final PointService pointService;
    private final PointRepository pointRepository;

    @Operation(summary = "회원별 포인트 내역 조회",
            description = "특정 회원의 포인트 내역을 조회합니다.",
            tags = {"point"})
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/{memberId}")
    public ResponseEntity<List<Point>> getPointsByMember(@PathVariable Long memberId) {
        List<Point> points = pointService.getPointsByMember(memberId);
        return ResponseEntity.ok(points);
    }


}
