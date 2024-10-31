package com.conseller.conseller.core.barter.infrastructure.entity;

import com.conseller.conseller.core.barter.api.dto.request.BarterModifyRequest;
import com.conseller.conseller.core.barter.api.dto.response.BarterHostItemResponse;
import com.conseller.conseller.core.barter.domain.Barter;
import com.conseller.conseller.core.barter.domain.enums.BarterStatus;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonEntity;
import com.conseller.conseller.core.category.infrastructure.SubCategoryEntity;
import com.conseller.conseller.core.user.infrastructure.UserEntity;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.conseller.conseller.global.utils.DateTimeConverter.convertString;
import static java.time.LocalDateTime.now;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "barterIdx")
public class BarterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long barterIdx;

    @Column(name = "barter_name", nullable = false)
    private String barterName;

    @Column(name = "barter_text", nullable = false)
    private String barterText;

    @CreatedDate
    private LocalDateTime barterCreatedDate;

    @Column(name = "barter_end_date", nullable = false)
    private LocalDateTime barterEndDate;

    @LastModifiedDate
    private LocalDateTime barterModifiedDate;

    @Column(name = "barter_completed_date")
    private LocalDateTime barterCompletedDate;

    private String barterStatus = BarterStatus.EXCHANGEABLE.getStatus();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_idx")
    private UserEntity barterHost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "complete_guest_idx")
    private UserEntity barterCompleteGuest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_idx")
    private SubCategoryEntity subCategoryEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prefer_sub_catergory_idx")
    private SubCategoryEntity preferSubCategoryEntity;

    public Barter toDomain() {
        return Barter.builder()
                .barterIdx(barterIdx)
                .barterName(barterName)
                .barterText(barterText)
                .barterCreatedDate(barterCreatedDate)
                .barterEndDate(barterEndDate)
                .barterModifiedDate(barterModifiedDate)
                .barterCompletedDate(barterCompletedDate)
                .barterStatus(barterStatus)
                .host(barterHost.toDomain())
                .barterCompleteGuest(barterCompleteGuest.toDomain())
                .subCategory(subCategoryEntity.toDomain())
                .preferSubCategory(preferSubCategoryEntity.toDomain())
                .build();
    }

    public static BarterEntity of(Barter barter) {
        return BarterEntity.builder()
                .barterIdx(barter.getBarterIdx())
                .barterName(barter.getBarterName())
                .barterText(barter.getBarterText())
                .barterCreatedDate(barter.getBarterCreatedDate())
                .barterEndDate(barter.getBarterEndDate())
                .barterModifiedDate(barter.getBarterModifiedDate())
                .barterCompletedDate(barter.getBarterCompletedDate())
                .barterStatus(barter.getBarterStatus())
                .barterHost(UserEntity.of(barter.getHost()))
                .barterCompleteGuest(UserEntity.of(barter.getBarterCompleteGuest()))
                .subCategoryEntity(SubCategoryEntity.of(barter.getSubCategory()))
                .preferSubCategoryEntity(SubCategoryEntity.of(barter.getPreferSubCategory()))
                .build();
    }

    public void modifyBarter(BarterModifyRequest barterModifyRequest, SubCategoryEntity preferSubCategoryEntity) {
        this.barterName = barterModifyRequest.getBarterName();
        this.barterText = barterModifyRequest.getBarterText();
        this.preferSubCategoryEntity = preferSubCategoryEntity;

    }
}

