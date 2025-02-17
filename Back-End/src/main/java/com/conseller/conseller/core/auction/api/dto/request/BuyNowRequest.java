package com.conseller.conseller.core.auction.api.dto.request;

import com.conseller.conseller.core.auction.domain.TargetAuction;
import com.conseller.conseller.core.auction.domain.TargetBuyer;
import lombok.Getter;

@Getter
public class BuyNowRequest {
    private long auctionIdx;
    private long hostIdx;
    private long buyerIdx;
    private int price;

    public TargetAuction toTargetAuction() {
        return new TargetAuction(auctionIdx, hostIdx);
    }

    public TargetBuyer toTargetBuyer() {
        return new TargetBuyer(buyerIdx, price);
    }
}
