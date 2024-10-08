package com.conseller.conseller.core.barter.infrastructure;

import com.conseller.conseller.core.barter.api.dto.request.BarterGuestItemDto;
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
public class BarterGuestItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long barterGuestItemIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barter_request_idx", nullable = false)
    private BarterRequestEntity barterRequestEntity;

    @OneToOne
    @JoinColumn(name = "gifticon_idx", nullable = false)
    private GifticonEntity gifticonEntity;

    @Builder
    public BarterGuestItem(BarterRequestEntity barterRequestEntity, GifticonEntity gifticonEntity) {
        this.barterRequestEntity = barterRequestEntity;
        this.gifticonEntity = gifticonEntity;
    }

    public BarterGuestItemDto toBarterGuestItemDto(BarterGuestItem barterGuestItem) {
        GifticonEntity gifticonEntity = barterGuestItem.getGifticonEntity();
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

        return BarterGuestItemDto.builder()
                .barterRequestIdx(barterGuestItem.getBarterRequestEntity().getBarterRequestIdx())
                .gifticon(gifticonResponse)
                .build();
    }
}
