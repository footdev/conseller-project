package com.conseller.conseller.core.barter.infrastructure.entity;

import com.conseller.conseller.core.barter.api.dto.request.BarterGuestItemRequest;
import com.conseller.conseller.core.barter.domain.BarterGuestItem;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonEntity;
import com.conseller.conseller.core.gifticon.api.dto.response.GifticonResponse;
import lombok.*;

import javax.persistence.*;

import static com.conseller.conseller.global.utils.DateTimeConverter.convertString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "barterGuestItemIdx")
public class BarterGuestItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long barterGuestItemIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barter_request_idx", nullable = false)
    private BarterRequestEntity barterRequestEntity;

    @OneToOne
    @JoinColumn(name = "gifticon_idx", nullable = false)
    private GifticonEntity gifticonEntity;

    @Column(name = "is_deleted", columnDefinition = "TINYINT(1)")
    private Boolean isDeleted;

    @Builder
    public BarterGuestItemEntity(BarterRequestEntity barterRequestEntity, GifticonEntity gifticonEntity, Boolean isDeleted) {
        this.barterRequestEntity = barterRequestEntity;
        this.gifticonEntity = gifticonEntity;
        this.isDeleted = isDeleted;
    }

    public BarterGuestItem toDomain() {
        return BarterGuestItem.builder()
                .barterGuestItemIdx(this.barterGuestItemIdx)
                .barterRequest(this.barterRequestEntity.toDomain())
                .gifticon(this.gifticonEntity.toDomain())
                .isDeleted(this.isDeleted)
                .build();
    }

    public static BarterGuestItemEntity of(BarterGuestItem barterGuestItem) {
        return BarterGuestItemEntity.builder()
                .barterRequestEntity(BarterRequestEntity.of(barterGuestItem.getBarterRequest()))
                .gifticonEntity(GifticonEntity.of(barterGuestItem.getGifticon()))
                .isDeleted(barterGuestItem.getIsDeleted())
                .build();
    }

    public BarterGuestItemRequest toBarterGuestItemDto(BarterGuestItemEntity barterGuestItemEntity) {
        GifticonEntity gifticonEntity = barterGuestItemEntity.getGifticonEntity();
        GifticonResponse gifticonResponse = GifticonResponse.builder()
                .gifticonIdx(gifticonEntity.getGifticonIdx())
                .gifticonBarcode(gifticonEntity.getGifticonBarcode())
                .gifticonName(gifticonEntity.getGifticonName())
                .gifticonStatus(gifticonEntity.getGifticonStatus())
                .gifticonAllImageUrl(gifticonEntity.getGifticonAllImageUrl())
                .gifticonDataImageUrl(gifticonEntity.getGifticonDataImageUrl())
                .gifticonStartDate(convertString(gifticonEntity.getGifticonStartDate()))
                .gifticonEndDate(convertString(gifticonEntity.getGifticonEndDate()))
                .mainCategoryIdx(gifticonEntity.getMainCategoryEntity().getMainCategoryIdx())
                .subCategoryIdx(gifticonEntity.getSubCategoryEntity().getSubCategoryIdx())
                .userIdx(gifticonEntity.getUserEntity().getUserIdx())
                .build();

        return BarterGuestItemRequest.builder()
                .barterRequestIdx(barterGuestItemEntity.getBarterRequestEntity().getBarterRequestIdx())
                .gifticon(gifticonResponse)
                .build();
    }
}
