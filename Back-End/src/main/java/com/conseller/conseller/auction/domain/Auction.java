package com.conseller.conseller.auction.domain;


import com.conseller.conseller.entity.AuctionBid;
import com.conseller.conseller.entity.Gifticon;
import com.conseller.conseller.user.infrastructure.UserEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
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
    private Gifticon gifticon;
    private UserEntity userEntity;
    private AuctionBid highestBid;
    private List<AuctionBid> auctionBidList;
}
