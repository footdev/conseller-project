package com.conseller.conseller.core.auction.implement;

import com.conseller.conseller.core.auction.domain.Auction;
import com.conseller.conseller.core.auction.infrastructure.AuctionEntity;
import com.conseller.conseller.core.auction.infrastructure.AuctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuctionAppender {
    private final AuctionRepository auctionRepository;

    public long append(Auction auction) {
        return auctionRepository.save(AuctionEntity.of(auction)).getAuctionIdx();
    }
}
