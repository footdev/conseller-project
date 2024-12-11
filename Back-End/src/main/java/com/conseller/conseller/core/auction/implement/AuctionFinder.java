package com.conseller.conseller.core.auction.implement;

import com.conseller.conseller.core.auction.domain.Auction;
import com.conseller.conseller.core.auction.infrastructure.AuctionRepository;
import com.conseller.conseller.core.gifticon.implement.GifticonValidator;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuctionFinder {
    private final AuctionRepository auctionRepository;
    private final AuctionValidator auctionValidator;
    private final GifticonValidator gifticonValidator;

    public Auction findProgressAuction(long auctionIdx) {
        Auction auction = auctionRepository.findById(auctionIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.AUCTION_INVALID))
                .toDomain();

        auctionValidator.isInProgress(auction);
        gifticonValidator.isValidGiftion(auction.getGifticon());

        return auction;
    }

    public Auction findConfirmedBidAuction(long auctionIdx) {
        Auction auction = auctionRepository.findById(auctionIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.AUCTION_INVALID))
                .toDomain();

        auctionValidator.isConfirmedBid(auction);
        return auction;
    }

    public List<Auction> findAuctionWithOneDayLeft() {
        return null;
    }
}
