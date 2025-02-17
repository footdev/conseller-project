package com.conseller.conseller.core.inquiry.domain;

import com.conseller.conseller.core.inquiry.domain.enums.InquiryStatus;
import com.conseller.conseller.core.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter @Builder
public class Inquiry {
    private long inquiryIdx;
    private long userIdx;
    private long reportedUserIdx;
    private InquiryStatus inquiryStatus;
    private String inquiryTitle;
    private String inquiryContent;
    private String inquiryAnswer;
    private LocalDateTime inquiryAnswerDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isDeleted;
    private int inquiryType;

    public static Inquiry of(User user, User reportedUser,AddInquiry addInquiry){
        return Inquiry.builder()
                .userIdx(user.getUserIdx())
                .reportedUserIdx(reportedUser.getUserIdx())
                .inquiryStatus(InquiryStatus.CHECKING)
                .inquiryTitle(addInquiry.getInquiryTitle())
                .inquiryContent(addInquiry.getInquiryContent())
                .inquiryType(addInquiry.getInquiryType())
                .build();
    }

    public void updateAnswer(String inquiryAnswer) {
        this.inquiryAnswer = inquiryAnswer;
    }
}
