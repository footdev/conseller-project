package com.conseller.conseller.core.bid.infrastructure;

import com.conseller.conseller.core.auction.infrastructure.AuctionEntity;
import com.conseller.conseller.core.bid.domain.enums.BidStatus;
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
@ToString
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(of = "auctionBidIdx")
public class AuctionBid {

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

}
