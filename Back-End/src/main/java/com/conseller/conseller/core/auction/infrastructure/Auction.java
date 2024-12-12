package com.conseller.conseller.core.auction.infrastructure;

import com.conseller.conseller.core.auction.domain.enums.AuctionStatus;
import com.conseller.conseller.core.bid.infrastructure.AuctionBidEntity;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonEntity;
import com.conseller.conseller.core.user.infrastructure.UserEntity;
import com.conseller.conseller.global.entity.BaseTimeEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@SuperBuilder
@Getter @Setter
@DynamicUpdate
@RequiredArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Auction extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auctionIdx;

    @Column(name = "auction_text", nullable = false)
    private String auctionText;

    @Column(name = "lower_price", nullable = false)
    private Integer lowerPrice;

    @Column(name = "upper_price", nullable = false)
    private Integer upperPrice;

    @Column(name = "auction_status", nullable = false)
    private String auctionStatus = AuctionStatus.IN_PROGRESS.getStatus();

    @CreatedDate
    private LocalDateTime auctionStartDate;

    @Column(name = "auction_end_date")
    private LocalDateTime auctionEndDate;

    @Column(name = "auction_completed_date")
    private LocalDateTime auctionCompletedDate;

    @Column(name = "notification_created_date")
    private LocalDateTime notificationCreatedDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gifticon_idx")
    private GifticonEntity gifticonEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private UserEntity userEntity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_bid_idx")
    private AuctionBidEntity highestBidEntity;

     public com.conseller.conseller.core.auction.domain.Auction toDomain() {
        return com.conseller.conseller.core.auction.domain.Auction.builder()
                .auctionIdx(auctionIdx)
                .auctionText(auctionText)
                .lowerPrice(lowerPrice)
                .upperPrice(upperPrice)
                .auctionStatus(AuctionStatus.valueOf(auctionStatus))
                .auctionStartDate(auctionStartDate)
                .auctionEndDate(auctionEndDate)
                .auctionCompletedDate(auctionCompletedDate)
                .notificationCreatedDate(notificationCreatedDate)
                .gifticon(gifticonEntity.toDomain())
                .user(userEntity.toDomain())
                .highestBid(highestBidEntity.toDomain())
                .isDeleted(super.getIsDeleted())
                .build();
    }

    public static Auction of(com.conseller.conseller.core.auction.domain.Auction auction) {
        return Auction.builder()
                .auctionIdx(auction.getAuctionIdx())
                .auctionText(auction.getAuctionText())
                .lowerPrice(auction.getLowerPrice())
                .upperPrice(auction.getUpperPrice())
                .auctionStatus(auction.getAuctionStatus().getStatus())
                .auctionStartDate(auction.getAuctionStartDate())
                .auctionEndDate(auction.getAuctionEndDate())
                .auctionCompletedDate(auction.getAuctionCompletedDate())
                .notificationCreatedDate(auction.getNotificationCreatedDate())
                .gifticonEntity(GifticonEntity.of(auction.getGifticon()))
                .userEntity(UserEntity.of(auction.getUser()))
                .highestBidEntity(AuctionBidEntity.of(auction.getHighestBid()))
                .isDeleted(auction.isDeleted())
                .build();
    }
}

