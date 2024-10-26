package com.conseller.conseller.core.barter.api.dto.response;

import com.conseller.conseller.core.barter.infrastructure.entity.BarterHostItemEntity;
import lombok.*;


@Getter
@Builder
public class BarterHostItemResponse {
    private long barterIdx;
    private String gifticonDataImageName;
    private String gifticonName;
    private String gifticonEndDate;
    private String barterEndDate;
    private Boolean deposit;
    private String preper;
    private String barterName;

    public static BarterHostItemResponse of(BarterHostItemEntity barterHostItemEntity) {
        return BarterHostItemResponse.builder()
                .barterIdx(barterHostItemEntity.getBarterEntity().getBarterIdx())
                .gifticonDataImageName(barterHostItemEntity.getGifticon().getGifticonDataImageName())
                .gifticonName(barterHostItemEntity.getGifticon().getGifticonName())
                .gifticonEndDate(barterHostItemEntity.getGifticon().getGifticonEndDate())
                .barterEndDate(barterHostItemEntity.getBarterEntity().getBarterEndDate().toString())
                .deposit(barterHostItemEntity.getGifticon().getDeposit())
                .preper(barterHostItemEntity.getGifticon().getPreper())
                .barterName(barterHostItemEntity.getBarterEntity().getBarterName())
                .build();
    }

}
