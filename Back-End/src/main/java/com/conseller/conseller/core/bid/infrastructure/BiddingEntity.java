package com.conseller.conseller.core.bid.infrastructure;

import com.conseller.conseller.core.bid.domain.enums.BidStatus;
import com.conseller.conseller.core.bid.domain.Bidding;
import com.conseller.conseller.global.entity.BaseTimeEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Getter @SuperBuilder
@Table(name = "auction_bid")
@EntityListeners(AuditingEntityListener.class)
public class BiddingEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auctionBidIdx;

    @Column(name = "auction_bid_price", nullable = false)
    private Integer auctionBidPrice;

    @Column(name = "auction_bid_status", nullable = false)
    private String auctionBidStatus = BidStatus.BIDED.getStatus();

    @Column(name = "user_idx", nullable = false)
    private Long userKey;

    @Column(name = "auction_idx", nullable = false)
    private Long auctionKey;

    public Bidding toDomain() {
        return Bidding.builder()
                .auctionBidIdx(auctionBidIdx)
                .biddingPrice(auctionBidPrice)
                .auctionBidStatus(BidStatus.valueOf(auctionBidStatus))
                .userId(userKey)
                .auctionId(auctionKey)
                .isDeleted(super.getIsDeleted())
                .build();
    }

    public static BiddingEntity of(Bidding bidding) {
        return BiddingEntity.builder()
                .auctionBidIdx(bidding.getAuctionBidIdx())
                .auctionBidPrice(bidding.getBiddingPrice())
                .auctionBidStatus(bidding.getAuctionBidStatus().getStatus())
                .userKey(bidding.getUserId())
                .auctionKey(bidding.getAuctionId())
                .createdAt(bidding.getCreatedAt())
                .isDeleted(bidding.isDeleted())
                .build();
    }
}
