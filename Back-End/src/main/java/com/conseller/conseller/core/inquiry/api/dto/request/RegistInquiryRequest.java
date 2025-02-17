package com.conseller.conseller.core.inquiry.api.dto.request;

import com.conseller.conseller.core.inquiry.domain.AddInquiry;
import lombok.Getter;

@Getter
public class RegistInquiryRequest {
    private long userIdx;
    private long reportedUserIdx;
    private String inquiryTitle;
    private String inquiryContent;
    private int inquiryType;

    public AddInquiry toAddInquiry() {
        return new AddInquiry(inquiryTitle, inquiryContent, inquiryType);
    }
}
