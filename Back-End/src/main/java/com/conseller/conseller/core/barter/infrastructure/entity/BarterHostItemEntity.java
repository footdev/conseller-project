package com.conseller.conseller.core.barter.infrastructure.entity;

import com.conseller.conseller.core.barter.api.dto.request.BarterHostItemDto;
import com.conseller.conseller.core.barter.domain.BarterHostItem;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonEntity;
import com.conseller.conseller.core.gifticon.api.dto.response.GifticonResponse;
import lombok.*;

import javax.persistence.*;

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

    @Builder
    public BarterHostItemEntity(BarterEntity barterEntity, GifticonEntity gifticonEntity) {
        this.barterEntity = barterEntity;
        this.gifticonEntity = gifticonEntity;
    }

    public BarterHostItem toDomain() {
        return BarterHostItem.builder()
                .barterHostItemIdx(barterHostItemIdx)
                .barter(barterEntity.toDomain())
                .gifticon(gifticonEntity.toDomain())
                .build();
    }

    public static BarterHostItemEntity of(BarterHostItem barterHostItem) {
        return BarterHostItemEntity.builder()
                .barterEntity(BarterEntity.of(barterHostItem.getBarter()))
                .gifticonEntity(GifticonEntity.of(barterHostItem.getGifticon()))
                .build();
    }

    public BarterHostItemDto toBarterHostItemDto(BarterEntity barterEntity, GifticonResponse gifticon) {
        BarterHostItemDto barterHostItemDto = new BarterHostItemDto();
        barterHostItemDto.setBarterIdx(barterEntity.getBarterIdx());
        barterHostItemDto.setGifticon(gifticon);

        return barterHostItemDto;
    }

    public BarterHostItemDto toBarterHostItemDto(BarterHostItemEntity barterHostItemEntity) {
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

        return BarterHostItemDto.builder()
                .barterIdx(barterHostItemEntity.getBarterEntity().getBarterIdx())
                .gifticon(gifticonResponse)
                .build();

    }
}
