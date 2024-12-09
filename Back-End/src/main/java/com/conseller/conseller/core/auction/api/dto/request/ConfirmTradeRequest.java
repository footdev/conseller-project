package com.conseller.conseller.core.auction.api.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class ConfirmTradeRequest {
    private final long auctionIdx;
    private final long buyerIdx;
}
