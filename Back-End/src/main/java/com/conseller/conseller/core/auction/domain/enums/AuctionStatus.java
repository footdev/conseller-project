package com.conseller.conseller.core.auction.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuctionStatus {
    IN_PROGRESS("진행 중"),
    IN_TRADE("거래 중"),
    BID_CONFIRMED("낙찰 확정"),
    TRADED("거래 확정"),
    EXPIRED("만료"),
    CANCLED("취소");

    private final String status;
}
