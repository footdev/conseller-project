package com.conseller.conseller.core.auction.infrastructure;

import com.conseller.conseller.core.auction.domain.SellerPaymentHistory;
import com.conseller.conseller.global.entity.BaseTimeEntity;
import lombok.Builder;

import javax.persistence.*;

@Entity
@Builder
public class SellerPaymentHistoryEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long auctionIdx;

    @Column(name = "user_idx", nullable = false)
    private Long sellerIdx;

    @Column(nullable = false)
    private Long auctionOrderIdx;

    public static SellerPaymentHistoryEntity of(SellerPaymentHistory sellerPaymentHistory) {
        return SellerPaymentHistoryEntity.builder()
                .id(sellerPaymentHistory.getId())
                .auctionIdx(sellerPaymentHistory.getAuctionIdx())
                .sellerIdx(sellerPaymentHistory.getSellerIdx())
                .auctionOrderIdx(sellerPaymentHistory.getAuctionOrderIdx())
                .build();
    }

    public SellerPaymentHistory toDomain() {
        return SellerPaymentHistory.builder()
                .id(id)
                .auctionIdx(auctionIdx)
                .sellerIdx(sellerIdx)
                .auctionOrderIdx(auctionOrderIdx)
                .build();
    }

}
