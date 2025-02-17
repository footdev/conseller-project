package com.conseller.conseller.core.inquiry.implement;

import com.conseller.conseller.core.inquiry.domain.AddInquiry;
import com.conseller.conseller.core.inquiry.domain.Inquiry;
import com.conseller.conseller.core.inquiry.infrastructure.InquiryEntity;
import com.conseller.conseller.core.inquiry.infrastructure.InquiryRepository;
import com.conseller.conseller.core.user.domain.User;
import com.conseller.conseller.core.user.implement.UserFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InquiryAppender {
    private final InquiryRepository inquiryRepository;
    private final UserFinder userFinder;
    private final InquiryReader inquiryReader;

    @Transactional
    public void append(long userIdx, long reportedUserIdx, AddInquiry addInquiry) {
        User user = userFinder.findValidUser(userIdx);
        User reportedUser = userFinder.findValidUser(reportedUserIdx);
        inquiryRepository.save(InquiryEntity.of(user, reportedUser, addInquiry));
    }

    @Transactional
    public void appendAnswer(long inquiryIdx, String inquiryAnswer) {
        Inquiry inquiry = inquiryReader.read(inquiryIdx);
        inquiry.updateAnswer(inquiryAnswer);
        inquiryRepository.save(InquiryEntity.of(inquiry));
    }
}
