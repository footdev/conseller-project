package com.conseller.conseller.core.inquiry.api.dto.response;

import com.conseller.conseller.core.inquiry.domain.Inquiry;
import lombok.*;

import java.time.LocalDateTime;

@Getter@Builder
@AllArgsConstructor
public class DetailInquiryResponse {
    private String inquiryTitle;
    private String inquiryContent;
    private String inquiryAnswer;
    private LocalDateTime inquiryAnswerDate;
    private String inquiryStatus;
    private Integer inquiryType;

    public static DetailInquiryResponse of(Inquiry inquiry) {
        return DetailInquiryResponse.builder()
                .inquiryTitle(inquiry.getInquiryTitle())
                .inquiryContent(inquiry.getInquiryContent())
                .inquiryAnswer(inquiry.getInquiryAnswer())
                .inquiryAnswerDate(inquiry.getInquiryAnswerDate())
                .inquiryStatus(inquiry.getInquiryStatus().getStatus())
                .inquiryType(inquiry.getInquiryType())
                .build();
    }
}
