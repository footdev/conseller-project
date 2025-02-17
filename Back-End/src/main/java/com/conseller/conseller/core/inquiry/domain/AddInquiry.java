package com.conseller.conseller.core.inquiry.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddInquiry {
    private String inquiryTitle;
    private String inquiryContent;
    private int inquiryType;
}
