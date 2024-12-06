package com.conseller.conseller.core.auction.domain;

import com.conseller.conseller.core.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class AuctionOrder {
    private Long auctionOrderId;
    private Long auctionIdx;
    private Long buyerIdx;
    private Integer price;
    private String orderNumber;

    public static AuctionOrder of(Auction auction, User buyer, String orderNumber) {
        return AuctionOrder.builder()
                .auctionIdx(auction.getAuctionIdx())
                .buyerIdx(buyer.getUserIdx())
                .price(auction.getUpperPrice())
                .orderNumber(orderNumber)
                .build();
    }
}
