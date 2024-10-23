package com.conseller.conseller.core.auction.implement;

import com.conseller.conseller.core.auction.domain.Auction;
import com.conseller.conseller.core.auction.infrastructure.AuctionEntity;
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
        Auction auction = auctionReader.read(id);
        auction.updateText(text);
        auctionRepository.save(AuctionEntity.of(auction));
    }
}
