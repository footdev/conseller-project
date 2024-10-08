package com.conseller.conseller.core.auction.domain;


import com.conseller.conseller.core.auction.infrastructure.AuctionEntity;
import com.conseller.conseller.core.bid.infrastructure.AuctionBid;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonEntity;
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
    private Long auctionIdx;
    private String auctionText;
    private Integer lowerPrice;
    private Integer upperPrice;
    private String auctionStatus;
    private LocalDateTime auctionStartDate;
    private LocalDateTime auctionEndDate;
    private LocalDateTime auctionCompletedDate;
    private LocalDateTime notificationCreatedDate;
    private GifticonEntity gifticonEntity;
    private User user;
    private AuctionBid highestBid;
    private List<AuctionBid> auctionBidList;

    public static Auction of(AuctionEntity auctionEntity) {
        return Auction.builder()
                .auctionIdx(auctionEntity.getAuctionIdx())
                .auctionText(auctionEntity.getAuctionText())
                .lowerPrice(auctionEntity.getLowerPrice())
                .upperPrice(auctionEntity.getUpperPrice())
                .auctionStatus(auctionEntity.getAuctionStatus())
                .auctionStartDate(auctionEntity.getAuctionStartDate())
                .auctionEndDate(auctionEntity.getAuctionEndDate())
                .auctionCompletedDate(auctionEntity.getAuctionCompletedDate())
                .notificationCreatedDate(auctionEntity.getNotificationCreatedDate())
                .gifticonEntity(auctionEntity.getGifticonEntity())
                .user(User.of(auctionEntity.getUserEntity()))
                .highestBid(auctionEntity.getHighestBid())
                .auctionBidList(auctionEntity.getAuctionBidList())
                .build();
    }
}
