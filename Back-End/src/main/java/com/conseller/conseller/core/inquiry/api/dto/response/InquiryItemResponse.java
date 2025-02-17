package com.conseller.conseller.core.inquiry.api.dto.response;

import com.conseller.conseller.core.inquiry.domain.Inquiry;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Builder
public class InquiryItemResponse {
    private long userIdx;
    private long inquiryIdx;
    private String inquiryTitle;
    private LocalDateTime createdAt;
    private int inquiryType;

    public static InquiryItemResponse of(Inquiry inquiry) {
        return InquiryItemResponse.builder()
                .userIdx(inquiry.getUserIdx())
                .inquiryIdx(inquiry.getInquiryIdx())
                .inquiryTitle(inquiry.getInquiryTitle())
                .createdAt(inquiry.getCreatedAt())
                .inquiryType(inquiry.getInquiryType())
                .build();
    }

    public static List<InquiryItemResponse> ofAll(List<Inquiry> inquiries) {
        return inquiries.stream()
                .map(InquiryItemResponse::of)
                .collect(Collectors.toList());
    }
}
