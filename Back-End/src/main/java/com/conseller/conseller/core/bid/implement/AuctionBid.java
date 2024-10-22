package com.conseller.conseller.core.bid.implement;

import com.conseller.conseller.core.auction.infrastructure.AuctionEntity;
import com.conseller.conseller.core.bid.domain.enums.BidStatus;
import com.conseller.conseller.core.user.infrastructure.UserEntity;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class AuctionBid {
    private Long auctionBidIdx;
    private Integer auctionBidPrice;
    private LocalDateTime auctionRegistedDate;
    private String auctionBidStatus;
    private UserEntity userEntity;
    private AuctionEntity auctionEntity;
}
