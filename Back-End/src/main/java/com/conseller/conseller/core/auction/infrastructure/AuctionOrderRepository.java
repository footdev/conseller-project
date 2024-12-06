package com.conseller.conseller.core.auction.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionOrderRepository extends JpaRepository<AuctionOrderEntity, Long> {
    public boolean existsByOrderNumber(String orderNumber);
}
