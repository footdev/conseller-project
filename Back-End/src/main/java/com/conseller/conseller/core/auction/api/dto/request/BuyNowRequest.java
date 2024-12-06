package com.conseller.conseller.core.auction.api.dto.request;

import com.conseller.conseller.core.auction.domain.TargetAuction;
import com.conseller.conseller.core.auction.domain.TargetBuyer;
import lombok.Getter;

import javax.validation.Valid;

@Getter
public class BuyNowRequest {
    private long auctionidx;
    private long hostIdx;
    private long buyerIdx;
    private int price;

    public TargetAuction toTargetAuction() {
        return new TargetAuction(auctionidx, hostIdx);
    }

    public TargetBuyer toTargetBuyer() {
        return new TargetBuyer(buyerIdx, price);
    }
}
