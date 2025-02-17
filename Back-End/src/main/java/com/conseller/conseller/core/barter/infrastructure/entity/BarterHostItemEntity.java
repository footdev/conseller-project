package com.conseller.conseller.core.barter.infrastructure.entity;

import com.conseller.conseller.core.barter.api.dto.request.BarterHostItemRequest;
import com.conseller.conseller.core.barter.domain.BarterHostItem;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonEntity;
import com.conseller.conseller.core.gifticon.api.dto.response.GifticonResponse;
import lombok.*;

import javax.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.conseller.conseller.global.utils.DateTimeConverter.convertString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "barterHostItemIdx")
public class BarterHostItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long barterHostItemIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barter_idx", nullable = false)
    private BarterEntity barterEntity;

    @OneToOne
    @JoinColumn(name = "gifticon_idx", nullable = false)
    private GifticonEntity gifticonEntity;

    @Column(name = "is_deleted", columnDefinition = "TINYINT(1)")
    private Boolean isDeleted;

    @Builder
    public BarterHostItemEntity(BarterEntity barterEntity, GifticonEntity gifticonEntity, Boolean isDeleted) {
        this.barterEntity = barterEntity;
        this.gifticonEntity = gifticonEntity;
        this.isDeleted = isDeleted;
    }

    public BarterHostItem toDomain() {
        return BarterHostItem.builder()
                .barterHostItemIdx(this.barterHostItemIdx)
                .barter(this.barterEntity.toDomain())
                .gifticon(this.gifticonEntity.toDomain())
                .isDeleted(this.isDeleted)
                .build();
    }

    public static BarterHostItemEntity of(BarterHostItem barterHostItem) {
        return BarterHostItemEntity.builder()
                .barterEntity(BarterEntity.of(barterHostItem.getBarter()))
                .gifticonEntity(GifticonEntity.of(barterHostItem.getGifticon()))
                .isDeleted(barterHostItem.getIsDeleted())
                .build();
    }

    public static List<BarterHostItemEntity> of(List<BarterHostItem> barterHostItems) {
        return barterHostItems.stream()
                .map(BarterHostItemEntity::of)
                .collect(Collectors.toList());
    }

    public BarterHostItemRequest toBarterHostItemDto(BarterEntity barterEntity, GifticonResponse gifticon) {
        BarterHostItemRequest barterHostItemRequest = new BarterHostItemRequest();
        barterHostItemRequest.setBarterIdx(barterEntity.getBarterIdx());
        barterHostItemRequest.setGifticon(gifticon);

        return barterHostItemRequest;
    }

    public BarterHostItemRequest toBarterHostItemDto(BarterHostItemEntity barterHostItemEntity) {
        GifticonEntity gifticonEntity = barterHostItemEntity.getGifticonEntity();
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

        return BarterHostItemRequest.builder()
                .barterIdx(barterHostItemEntity.getBarterEntity().getBarterIdx())
                .gifticon(gifticonResponse)
                .build();

    }
}
