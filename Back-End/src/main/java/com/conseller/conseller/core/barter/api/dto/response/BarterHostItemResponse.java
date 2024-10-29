package com.conseller.conseller.core.barter.api.dto.response;

import com.conseller.conseller.core.barter.domain.BarterHostItem;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterHostItemEntity;
import lombok.*;

import static com.conseller.conseller.global.utils.DateTimeConverter.convertString;


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

    public static BarterHostItemResponse of(BarterHostItem barterhostItem) {
        return BarterHostItemResponse.builder()
                .barterIdx(barterhostItem.getBarter().getBarterIdx())
                .gifticonDataImageName(barterhostItem.getGifticon().getGifticonDataImageUrl())
                .gifticonName(barterhostItem.getGifticon().getGifticonName())
                .gifticonEndDate(convertString(barterhostItem.getGifticon().getGifticonEndDate()))
                .barterEndDate(barterhostItem.getBarter().getBarterEndDate().toString())
                .deposit(barterhostItem.getBarter().getBarterHost().getUserDeposit() > 0)
                .preper(barterhostItem.getBarter().getPreferSubCategory().getSubCategoryContent())
                .barterName(barterhostItem.getBarter().getBarterName())
                .build();
    }

}
