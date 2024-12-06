package com.conseller.conseller.core.auction.implement;

import com.conseller.conseller.core.auction.domain.Auction;
import com.conseller.conseller.core.auction.infrastructure.AuctionEntity;
import com.conseller.conseller.core.auction.infrastructure.AuctionRepository;
import com.conseller.conseller.core.gifticon.implement.GifticonValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuctionFinder {
    private final AuctionRepository auctionRepository;
    private final AuctionValidator auctionValidator;
    private final GifticonValidator gifticonValidator;

    public Auction findValidAuction(long auctionIdx) {
        Auction auction = auctionRepository.findById(auctionIdx)
                .orElseThrow(() -> new IllegalArgumentException("해당 경매가 존재하지 않습니다."))
                .toDomain();

        auctionValidator.isInProgress(auction);
        gifticonValidator.isValidGiftion(auction.getGifticon());

        return auction;
    }
}
