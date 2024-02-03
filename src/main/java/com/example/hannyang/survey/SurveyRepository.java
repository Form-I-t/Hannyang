package com.example.hannyang.survey;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {
    List<Survey> findByMemberMemberId(Long memberId);

    // 최신 등록 순으로 정렬하여 설문 목록을 가져옵니다.
    List<Survey> findAllByOrderByCreatedAtDesc();

    // 참여율이 높은 순으로 정렬하여 설문 목록을 가져옵니다.
    List<Survey> findAllByOrderByParticipantCountDesc();

    // 페이징된 데이터를 정렬하여 설문 목록을 가져옵니다.
    Page<Survey> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // 설문조사 포인트순으로 설문 목록을 가져옵니다.
    List<Survey> findAllByOrderByRewardPointsDesc();

    // 마감 기한 별로 설문 목록을 가져옵니다.
    List<Survey> findAllByOrderByDeadlineAsc();

    // 가성비 높은 순으로 설문 목록을 가져옵니다.
    @Query("SELECT s FROM Survey s WHERE s.questionCount > 0 ORDER BY s.rewardPoints / s.questionCount DESC")
    List<Survey> findAllOrderByValueForMoneyDesc();
}
