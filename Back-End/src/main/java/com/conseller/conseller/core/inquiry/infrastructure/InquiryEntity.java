package com.conseller.conseller.core.inquiry.infrastructure;

import com.conseller.conseller.core.inquiry.domain.AddInquiry;
import com.conseller.conseller.core.inquiry.domain.Inquiry;
import com.conseller.conseller.core.inquiry.domain.enums.InquiryStatus;
import com.conseller.conseller.core.user.domain.User;
import com.conseller.conseller.global.entity.BaseTimeEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @SuperBuilder
@EntityListeners(AuditingEntityListener.class)
public class InquiryEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inquiryIdx;

    @Column(name = "user_idx", nullable = false)
    private Long userIdx;

    @Column(name = "user_idx", nullable = false)
    private Long reportedUserIdx;

    @Column(name = "inquiry_status", nullable = false)
    private String inquiryStatus;

    @Column(nullable = false)
    private String inquiryTitle;

    @Column(nullable = false)
    private String inquiryContent;

    @Column(nullable = false)
    private String inquiryAnswer;

    @Column(nullable = false)
    private LocalDateTime inquiryAnswerDate;

    @Column(nullable = false)
    private Integer inquiryType;

    public static InquiryEntity of(User user, User reportedUser, AddInquiry addInquiry){
        return InquiryEntity.builder()
                .userIdx(user.getUserIdx())
                .reportedUserIdx(reportedUser.getUserIdx())
                .inquiryStatus(InquiryStatus.CHECKING.getStatus())
                .inquiryTitle(addInquiry.getInquiryTitle())
                .inquiryContent(addInquiry.getInquiryContent())
                .inquiryType(addInquiry.getInquiryType())
                .build();
    }

    public static InquiryEntity of(Inquiry inquiry) {
        return InquiryEntity.builder()
                .inquiryIdx(inquiry.getInquiryIdx())
                .userIdx(inquiry.getUserIdx())
                .reportedUserIdx(inquiry.getReportedUserIdx())
                .inquiryStatus(inquiry.getInquiryStatus().getStatus())
                .inquiryTitle(inquiry.getInquiryTitle())
                .inquiryContent(inquiry.getInquiryContent())
                .inquiryAnswer(inquiry.getInquiryAnswer())
                .inquiryAnswerDate(inquiry.getInquiryAnswerDate())
                .inquiryType(inquiry.getInquiryType())
                .build();
    }

    public Inquiry toDomain(){
        return Inquiry.builder()
                .inquiryIdx(inquiryIdx)
                .userIdx(userIdx)
                .reportedUserIdx(reportedUserIdx)
                .inquiryStatus(InquiryStatus.valueOf(inquiryStatus))
                .inquiryTitle(inquiryTitle)
                .inquiryContent(inquiryContent)
                .inquiryAnswer(inquiryAnswer)
                .inquiryAnswerDate(inquiryAnswerDate)
                .createdAt(getCreatedAt())
                .updatedAt(getUpdatedAt())
                .isDeleted(getIsDeleted())
                .inquiryType(inquiryType)
                .build();
    }

    public void updateInquiryAnswer(String inquiryAnswer) {
        this.inquiryAnswer = inquiryAnswer;
    }
}
