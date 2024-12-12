package com.conseller.conseller.core.auction.implement;

import com.conseller.conseller.core.auction.infrastructure.Auction;
import com.conseller.conseller.core.auction.infrastructure.AuctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuctionAppender {
    private final AuctionRepository auctionRepository;

    public long append(com.conseller.conseller.core.auction.domain.Auction auction) {
        return auctionRepository.save(Auction.of(auction)).getAuctionIdx();
    }
}
