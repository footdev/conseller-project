package com.conseller.conseller.core.auction.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerPaymentHistoryRepository extends JpaRepository<SellerPaymentHistoryEntity, Long> {
}
