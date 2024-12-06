package com.conseller.conseller.core.auction.domain;

import lombok.Getter;

@Getter
public class TargetBuyer {
    private long buyerIdx;
    private long price;

    public TargetBuyer(long buyerIdx) {
        this.buyerIdx = buyerIdx;
    }
}
