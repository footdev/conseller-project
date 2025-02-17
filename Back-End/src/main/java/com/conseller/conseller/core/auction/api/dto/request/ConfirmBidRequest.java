package com.conseller.conseller.core.auction.api.dto.request;

import com.conseller.conseller.core.auction.domain.TargetAuction;
import com.conseller.conseller.core.auction.domain.TargetBuyer;

public class ConfirmBidRequest {
    private long auctionIdx;
    private long ownerIdx;
    private long buyerIdx;

    public TargetAuction toTargetAuction() {
        return new TargetAuction(auctionIdx, ownerIdx);
    }

    public TargetBuyer toTargetBuyer() {
        return new TargetBuyer(auctionIdx);
    }
}
