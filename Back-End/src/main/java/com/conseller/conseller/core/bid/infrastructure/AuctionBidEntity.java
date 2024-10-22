package com.conseller.conseller.core.bid.infrastructure;

import com.conseller.conseller.core.auction.infrastructure.AuctionEntity;
import com.conseller.conseller.core.bid.domain.enums.BidStatus;
import com.conseller.conseller.core.bid.implement.AuctionBid;
import com.conseller.conseller.core.user.infrastructure.UserEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(of = "auctionBidIdx")
public class AuctionBidEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auctionBidIdx;

    @Column(name = "auction_bid_price", nullable = false)
    private Integer auctionBidPrice;

    @CreatedDate
    private LocalDateTime auctionRegistedDate;

    @Column(name = "auction_bid_status", nullable = false)
    private String auctionBidStatus = BidStatus.BIDED.getStatus();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_idx")
    private AuctionEntity auctionEntity;

    public AuctionBid toDomain() {
        return AuctionBid.builder()
                .auctionBidIdx(auctionBidIdx)
                .auctionBidPrice(auctionBidPrice)
                .auctionRegistedDate(auctionRegistedDate)
                .auctionBidStatus(auctionBidStatus)
                .userEntity(userEntity)
                .auctionEntity(auctionEntity)
                .build();
    }

    public static AuctionBidEntity of(AuctionBid auctionBid) {
        return AuctionBidEntity.builder()
                .auctionBidIdx(auctionBid.getAuctionBidIdx())
                .auctionBidPrice(auctionBid.getAuctionBidPrice())
                .auctionRegistedDate(auctionBid.getAuctionRegistedDate())
                .auctionBidStatus(auctionBid.getAuctionBidStatus())
                .userEntity(auctionBid.getUserEntity())
                .auctionEntity(auctionBid.getAuctionEntity())
                .build();
    }
}
