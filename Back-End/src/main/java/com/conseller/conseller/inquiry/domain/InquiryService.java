package com.conseller.conseller.inquiry.domain;

import com.conseller.conseller.inquiry.api.dto.request.AnswerInquiryRequest;
import com.conseller.conseller.inquiry.api.dto.request.RegistInquiryRequest;
import com.conseller.conseller.inquiry.api.dto.response.DetailInquiryResponse;
import com.conseller.conseller.inquiry.api.dto.response.InquiryListResponse;

public interface InquiryService {
    public void registInquiry(RegistInquiryRequest request);

    public InquiryListResponse getInquiryList();

    public DetailInquiryResponse detailInquiry(Long inquiryIdx);

    public void answerInquiry(Long inquiryIdx, AnswerInquiryRequest request);

}
