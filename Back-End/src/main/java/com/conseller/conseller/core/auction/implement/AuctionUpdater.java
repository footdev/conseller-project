package com.conseller.conseller.core.auction.implement;

import com.conseller.conseller.core.auction.infrastructure.Auction;
import com.conseller.conseller.core.auction.infrastructure.AuctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AuctionUpdater {
    private final AuctionRepository auctionRepository;
    private final AuctionReader auctionReader;

    @Transactional
    public void updateAuction(long id, String text) {
        com.conseller.conseller.core.auction.domain.Auction auction = auctionReader.read(id);
        auction.modifyText(text);
        auctionRepository.save(Auction.of(auction));
    }
}
