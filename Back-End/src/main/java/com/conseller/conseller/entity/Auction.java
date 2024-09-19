package com.conseller.conseller.entity;

import com.conseller.conseller.auction.auction.enums.AuctionStatus;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(of = "auctionIdx")
public class Auction {
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

    @OneToOne
    @JoinColumn(name = "gifticon_idx")
    private Gifticon gifticon;

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private User user;

    // TODO: 유저가 아니라 최고 입찰을 넣어야함.
    @ManyToOne
    @JoinColumn(name = "auction_bid_idx")
    private AuctionBid highestBid;

    @OneToMany(mappedBy = "auction")
    private List<AuctionBid> auctionBidList;
}

