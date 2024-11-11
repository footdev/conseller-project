package com.conseller.conseller.core.barter.domain;

import com.conseller.conseller.core.gifticon.domain.Gifticon;
import com.conseller.conseller.core.gifticon.infrastructure.enums.GifticonStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BarterGuestItem {
    private Long barterGuestItemIdx;
    private BarterRequest barterRequest;
    private Gifticon gifticon;
    private Boolean isDeleted;

    public void delete() {
        isDeleted = true;
        gifticon.updateStatus(GifticonStatus.KEEP);
    }
}
