package com.conseller.conseller.core.auction.domain;

import com.conseller.conseller.core.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class SellerPaymentHistory {
    private Long id;
    private Long auctionIdx;
    private Long sellerIdx;
    private Long auctionOrderIdx;

    public static SellerPaymentHistory of(Auction auction, User seller, AuctionOrder auctionOrder) {
        return SellerPaymentHistory.builder()
                .auctionIdx(auction.getAuctionIdx())
                .sellerIdx(seller.getUserIdx())
                .auctionOrderIdx(auctionOrder.getAuctionOrderId())
                .build();

    }
}
