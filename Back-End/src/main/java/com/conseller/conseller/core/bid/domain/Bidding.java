package com.conseller.conseller.core.bid.domain;

import com.conseller.conseller.core.auction.domain.Auction;
import com.conseller.conseller.core.bid.domain.enums.BidStatus;
import com.conseller.conseller.core.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class Bidding {
    private Long auctionBidIdx;
    private Integer biddingPrice;
    private BidStatus auctionBidStatus;
    private Long userId;
    private Long auctionId;
    private LocalDateTime createdAt;
    private boolean isDeleted;

    public static Bidding of(Auction auction, User user, AddBidding addBidding) {
        return Bidding.builder()
                .biddingPrice(addBidding.getPrice())
                .auctionBidStatus(BidStatus.BIDED)
                .userId(user.getUserIdx())
                .auctionId(auction.getAuctionIdx())
                .createdAt(addBidding.getCreatedAt())
                .isDeleted(false)
                .build();
    }

    public void award() {
        this.auctionBidStatus = BidStatus.AWARDED;
    }

    public void reject() {
        this.auctionBidStatus = BidStatus.FAILURE;
    }

    public void delete() {
        this.isDeleted = true;
    }
}
