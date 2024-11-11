package com.conseller.conseller.core.barter.domain;

import com.conseller.conseller.core.gifticon.domain.Gifticon;
import com.conseller.conseller.core.gifticon.infrastructure.enums.GifticonStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BarterHostItem {
    private long barterHostItemIdx;
    private Barter barter;
    private Gifticon gifticon;
    private Boolean isDeleted;

    public String getHostItemName() {
        return gifticon.getGifticonName();
    }

    public String getHostItemDataImageUrl() {
        return gifticon.getGifticonDataImageUrl();
    }

    public LocalDateTime getHostItemEndDate() {
        return gifticon.getGifticonEndDate();
    }

    public void delete() {
        this.isDeleted = true;
        this.gifticon.updateStatus(GifticonStatus.KEEP);
    }

    public static BarterHostItem from(Gifticon gifticon, Barter barter, Boolean isDeleted) {
        return BarterHostItem.builder()
                .gifticon(gifticon)
                .barter(barter)
                .isDeleted(isDeleted)
                .build();
    }
}
