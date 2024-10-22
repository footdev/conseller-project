package com.conseller.conseller.core.auction.domain;


import com.conseller.conseller.core.auction.infrastructure.AuctionEntity;
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
    private String auctionStatus;
    private LocalDateTime auctionStartDate;
    private LocalDateTime auctionEndDate;
    private LocalDateTime auctionCompletedDate;
    private LocalDateTime notificationCreatedDate;
    private Gifticon gifticon;
    private User user;
    private AuctionBidEntity highestBid;
    private List<AuctionBidEntity> auctionBidEntityList;

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
                .gifticon(auctionEntity.getGifticonEntity().toDomain())
                .user(auctionEntity.getUserEntity().toDomain())
                .highestBid(auctionEntity.getHighestBid())
                .auctionBidEntityList(auctionEntity.getAuctionBidEntityList())
                .build();
    }
}
