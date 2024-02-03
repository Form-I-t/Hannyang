package com.example.hannyang.member.dtos;

import com.example.hannyang.product_history.ProductHistory;
import com.example.hannyang.survey.Survey;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MemberProfileResponseDto {
    private final MemberResponseDto memberInfo;
    private final List<Survey> surveys;
    private final List<ProductHistory> productHistories;

    public MemberProfileResponseDto(MemberResponseDto memberInfo, List<Survey> surveys, List<ProductHistory> productHistories) {
        this.memberInfo = memberInfo;
        this.surveys = surveys;
        this.productHistories = productHistories;
    }
}
