package com.conseller.conseller.core.barter.domain;

import com.conseller.conseller.core.gifticon.domain.Gifticon;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BarterHostItem {
    private long barterHostItemIdx;
    private Barter barter;
    private Gifticon gifticon;
}
