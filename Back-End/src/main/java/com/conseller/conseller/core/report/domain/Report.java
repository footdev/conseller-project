package com.conseller.conseller.core.report.domain;

import com.conseller.conseller.core.user.domain.User;
import com.conseller.conseller.core.user.infrastructure.UserEntity;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter
@Builder
public class Report {
    private long reportIdx;
//    @Column(name = "report_category", nullable = false)
//    @Enumerated
//    private Enum reportCategory;
    private LocalDateTime reportCreatedDate;
    private LocalDateTime reportCompletedDate;
    private String reportText;
//    @Column(name = "report_status", nullable = false)
//    private Enum reportStatus;
    private User reporter;
    private User reported;
}
