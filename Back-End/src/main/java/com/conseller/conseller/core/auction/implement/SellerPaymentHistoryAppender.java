package com.conseller.conseller.core.auction.implement;

import com.conseller.conseller.core.auction.domain.Auction;
import com.conseller.conseller.core.auction.domain.AuctionOrder;
import com.conseller.conseller.core.auction.domain.SellerPaymentHistory;
import com.conseller.conseller.core.auction.infrastructure.SellerPaymentHistoryEntity;
import com.conseller.conseller.core.auction.infrastructure.SellerPaymentHistoryRepository;
import com.conseller.conseller.core.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SellerPaymentHistoryAppender {
    private final SellerPaymentHistoryRepository sellerPaymentHistoryRepository;

    public void append(Auction auction, User seller, AuctionOrder auctionOrder) {
        sellerPaymentHistoryRepository.save(SellerPaymentHistoryEntity.of(SellerPaymentHistory.of(auction, seller, auctionOrder)));
    }
}
