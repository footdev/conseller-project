package com.conseller.conseller.core.auction.implement;

import com.conseller.conseller.core.auction.domain.Auction;
import com.conseller.conseller.core.auction.domain.AuctionOrder;
import com.conseller.conseller.core.auction.infrastructure.AuctionOrderEntity;
import com.conseller.conseller.core.auction.infrastructure.AuctionOrderRepository;
import com.conseller.conseller.core.user.domain.User;
import com.conseller.conseller.global.utils.OrderNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuctionOrderAppender {
    private final AuctionOrderRepository auctionOrderRepository;
    private final OrderNumberGenerator orderNumberGenerator;

    public void append(Auction auction, User buyer) {
        AuctionOrder auctionOrder = AuctionOrder.of(auction, buyer, orderNumberGenerator.generateUniqueOrderNumber());
        auctionOrderRepository.save(AuctionOrderEntity.from(auctionOrder));
    }
}
