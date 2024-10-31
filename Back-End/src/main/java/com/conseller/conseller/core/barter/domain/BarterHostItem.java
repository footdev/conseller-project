package com.conseller.conseller.core.barter.domain;

import com.conseller.conseller.core.gifticon.domain.Gifticon;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BarterHostItem {
    private long barterHostItemIdx;
    private Barter barter;
    private Gifticon gifticon;

    public String getHostItemName() {
        return gifticon.getGifticonName();
    }

    public String getHostItemDataImageUrl() {
        return gifticon.getGifticonDataImageUrl();
    }

    public LocalDateTime getHostItemEndDate() {
        return gifticon.getGifticonEndDate();
    }

    public static BarterHostItem from(Gifticon gifticon, Barter barter) {
        return BarterHostItem.builder()
                .gifticon(gifticon)
                .barter(barter)
                .build();
    }
}
