package com.conseller.conseller.core.report.infrastructure;

import com.conseller.conseller.core.report.domain.Report;
import com.conseller.conseller.core.user.infrastructure.UserEntity;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "reportIdx")
public class ReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reportIdx;

//    @Column(name = "report_category", nullable = false)
//    @Enumerated
//    private Enum reportCategory;

    @CreatedDate
    private LocalDateTime reportCreatedDate;

    @LastModifiedDate
    private LocalDateTime reportCompletedDate;

    @Column(name = "report_text", nullable = false)
    private String reportText;

//    @Column(name = "report_status", nullable = false)
//    private Enum reportStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_idx")
    private UserEntity reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_idx")
    private UserEntity reported;

    public Report toDomain() {
        return Report.builder()
                .reportIdx(reportIdx)
                .reportCreatedDate(reportCreatedDate)
                .reportCompletedDate(reportCompletedDate)
                .reportText(reportText)
                .reporter(reporter.toDomain())
                .reported(reported.toDomain())
                .build();
    }

    public static  ReportEntity of(Report report) {
        return ReportEntity.builder()
                .reportIdx(report.getReportIdx())
                .reportCreatedDate(report.getReportCreatedDate())
                .reportCompletedDate(report.getReportCompletedDate())
                .reportText(report.getReportText())
                .reporter(UserEntity.of(report.getReporter()))
                .reported(UserEntity.of(report.getReported()))
                .build();
    }
}
