package com.conseller.conseller.auction.auction;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuctionOrderStatus {
    START_DATE (0),
    END_DATE(1),
    HIGHEST_BID(2),
    UPPER_PRICE(3);

    private final int status;

    public static AuctionOrderStatus findByStatus(int status) {
        for (AuctionOrderStatus value : values()) {
            if (value.getStatus() == status) {
                return value;
            }
        }
        return null;
    }
}
