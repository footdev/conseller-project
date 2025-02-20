package com.conseller.conseller.core.auction.domain;


import com.conseller.conseller.core.auction.api.dto.request.RegistAuctionRequest;
import com.conseller.conseller.core.auction.domain.enums.AuctionStatus;
import com.conseller.conseller.core.bid.domain.Bidding;
import com.conseller.conseller.core.bid.infrastructure.BiddingEntity;
import com.conseller.conseller.core.gifticon.domain.Gifticon;
import com.conseller.conseller.core.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class Auction {
    private long auctionIdx;
    private String auctionText;
    private Integer lowerPrice;
    private Integer upperPrice;
    private AuctionStatus auctionStatus;
    private LocalDateTime auctionStartDate;
    private LocalDateTime auctionEndDate;
    private LocalDateTime auctionCompletedDate;
    private LocalDateTime notificationCreatedDate;
    private Gifticon gifticon;
    private User user;
    private Bidding highestBidding;
    private List<BiddingEntity> biddingEntityList;
    private boolean isDeleted;

    public static Auction of(RegistAuctionRequest registAuctionRequest, User user, Gifticon gifticon) {
        return Auction.builder()
                .auctionText(registAuctionRequest.getAuctionText())
                .lowerPrice(registAuctionRequest.getLowerPrice())
                .upperPrice(registAuctionRequest.getUpperPrice())
                .auctionStatus(AuctionStatus.IN_PROGRESS)
                .auctionStartDate(LocalDateTime.now())
                .auctionEndDate(gifticon.getGifticonEndDate())
                .notificationCreatedDate(LocalDateTime.now().plusDays(7))
                .gifticon(gifticon)
                .user(user)
                .highestBidding(null)
                .isDeleted(false)
                .build();
    }

    public void modifyText(String text) {
        this.auctionText = text;
    }

    public boolean isInProgress() {
        return this.auctionStatus.equals(AuctionStatus.IN_PROGRESS);
    }

    public void trade() {
        this.auctionStatus = AuctionStatus.IN_TRADE;
    }

    public void delete() {
        this.auctionStatus = AuctionStatus.CANCLED;
        this.isDeleted = true;
    }

    public boolean isConfirmedBid() {
        return this.auctionStatus.equals(AuctionStatus.BID_CONFIRMED);
    }

    public void updateHighestBid(Bidding bidding) {
        this.highestBidding = bidding;
    }
}
