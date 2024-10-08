package com.conseller.conseller.core.auction.infrastructure;

import com.conseller.conseller.core.auction.domain.enums.AuctionStatus;
import com.conseller.conseller.core.bid.infrastructure.AuctionBid;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonEntity;
import com.conseller.conseller.core.user.infrastructure.UserEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@DynamicUpdate
@RequiredArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AuctionEntity {
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_bid_idx")
    private AuctionBid highestBid;

    @OneToMany(mappedBy = "auction")
    private List<AuctionBid> auctionBidList;
}

