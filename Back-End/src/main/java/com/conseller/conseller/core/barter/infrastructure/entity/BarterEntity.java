package com.conseller.conseller.core.barter.infrastructure.entity;

import com.conseller.conseller.core.barter.api.dto.request.BarterHostItemDto;
import com.conseller.conseller.core.barter.api.dto.request.BarterModifyRequestDto;
import com.conseller.conseller.core.barter.api.dto.response.BarterItemData;
import com.conseller.conseller.core.barter.api.dto.response.BarterResponseDTO;
import com.conseller.conseller.core.barter.domain.Barter;
import com.conseller.conseller.core.barter.domain.enums.BarterStatus;
import com.conseller.conseller.core.barter.api.dto.response.BarterRequestResponseDto;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonEntity;
import com.conseller.conseller.core.category.infrastructure.SubCategoryEntity;
import com.conseller.conseller.core.user.infrastructure.UserEntity;
import com.conseller.conseller.core.user.api.dto.response.UserInfoResponse;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

 // Enum의 문자열 값을 데이터베이스에 저장
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
                .barterHost(barterHost)
                .barterCompleteGuest(barterCompleteGuest)
                .subCategoryEntity(subCategoryEntity)
                .preferSubCategoryEntity(preferSubCategoryEntity)
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
                .barterHost(barter.getBarterHost())
                .barterCompleteGuest(barter.getBarterCompleteGuest())
                .subCategoryEntity(barter.getSubCategoryEntity())
                .preferSubCategoryEntity(barter.getPreferSubCategoryEntity())
                .build();
    }

    public void modifyBarter(BarterModifyRequestDto barterModifyRequestDto, SubCategoryEntity preferSubCategoryEntity) {
        this.barterName = barterModifyRequestDto.getBarterName();
        this.barterText = barterModifyRequestDto.getBarterText();
        this.preferSubCategoryEntity = preferSubCategoryEntity;

    }

    public BarterItemData toBarterItemData(BarterEntity barterEntity, GifticonEntity gifticonEntity, Boolean deposit) {
        return BarterItemData.builder()
                .barterIdx(barterEntity.getBarterIdx())
                .gifticonDataImageName(gifticonEntity.getGifticonDataImageUrl())
                .gifticonName(gifticonEntity.getGifticonName())
                .gifticonEndDate(convertString(gifticonEntity.getGifticonEndDate()))
                .barterEndDate(convertString(barterEntity.getBarterEndDate()))
                .deposit(deposit)
                .preper(barterEntity.getPreferSubCategoryEntity().getSubCategoryContent())
                .barterName(barterEntity.getBarterName())
                .build();
    }
}

