package com.conseller.conseller.core.auction.domain.enums;

import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
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
        throw new CustomException(CustomExceptionStatus.INVALID_AUCTION_ORDER_STATUS);
    }
}
