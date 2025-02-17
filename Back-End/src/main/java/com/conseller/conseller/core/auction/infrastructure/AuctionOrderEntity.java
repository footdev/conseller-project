package com.conseller.conseller.core.auction.infrastructure;

import com.conseller.conseller.core.auction.domain.AuctionOrder;
import com.conseller.conseller.global.entity.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter @Builder
public class AuctionOrderEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auctionOrderId;

    @Column(name = "auction_idx", nullable = false)
    private Long auctionIdx;

    @Column(name = "user_idx", nullable = false)
    private Long buyerIdx;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "order_number", nullable = false)
    private String orderNumber;

    public AuctionOrder toDomain() {
        return AuctionOrder.builder()
                .auctionOrderId(auctionOrderId)
                .auctionIdx(auctionIdx)
                .buyerIdx(buyerIdx)
                .price(price)
                .orderNumber(orderNumber)
                .build();
    }

    public static AuctionOrderEntity from(AuctionOrder auctionOrder) {
        return AuctionOrderEntity.builder()
                .auctionOrderId(auctionOrder.getAuctionOrderId())
                .auctionIdx(auctionOrder.getAuctionIdx())
                .buyerIdx(auctionOrder.getBuyerIdx())
                .price(auctionOrder.getPrice())
                .orderNumber(UUID.randomUUID().toString())
                .build();
    }
}
