package com.conseller.conseller.core.inquiry.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegistInquiryRequest {
    private Long userIdx;

    private String inquiryTitle;

    private String inquiryContent;

    private String inquiryType;

    private Long reportedUserIdx;
}
