package com.conseller.conseller.core.bid.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BidStatus {
    BIDED("입찰"),
    AWARDED("낙찰"),
    FAILURE("실패");

    private final String status;
}
