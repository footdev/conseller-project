package com.conseller.conseller.core.auction.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuctionOrderRepository extends JpaRepository<AuctionOrderEntity, Long> {
    public boolean existsByOrderNumber(String orderNumber);

    Optional<AuctionOrderEntity> findByAuctionIdxAndBuyerIdx(long auctionIdx, long buyerIdx);
}
