package com.conseller.conseller.core.inquiry.domain;

import com.conseller.conseller.core.inquiry.implement.InquiryAppender;
import com.conseller.conseller.core.inquiry.implement.InquiryReader;
import com.conseller.conseller.core.inquiry.infrastructure.InquiryEntity;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import com.conseller.conseller.core.inquiry.api.dto.request.AnswerInquiryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InquiryService {
    private final InquiryReader inquiryReader;
    private final InquiryAppender inquiryAppender;

    public void registInquiry(long userIdx, long reportedUserIdx, AddInquiry addInquiry) {
        inquiryAppender.append(userIdx, reportedUserIdx, addInquiry);
    }

    public List<Inquiry> getInquires() {
        return inquiryReader.readAll();
    }

    public Inquiry detailInquiry(long inquiryIdx) {
        return inquiryReader.read(inquiryIdx);
    }

    public void answerInquiry(long inquiryIdx, AnswerInquiryRequest request) {
        inquiryAppender.appendAnswer(inquiryIdx, request.getInquiryAnswer());
    }
}
