package com.conseller.conseller.core.auction.domain;


import com.conseller.conseller.core.auction.api.dto.request.RegistAuctionRequest;
import com.conseller.conseller.core.auction.domain.enums.AuctionStatus;
import com.conseller.conseller.core.auction.infrastructure.AuctionEntity;
import com.conseller.conseller.core.bid.implement.AuctionBid;
import com.conseller.conseller.core.bid.infrastructure.AuctionBidEntity;
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
    private AuctionBid highestBid;
    private List<AuctionBidEntity> auctionBidEntityList;

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
                .highestBid(null)
                .build();
    }

    public void updateText(String text) {
        this.auctionText = text;
    }
}
