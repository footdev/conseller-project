package com.conseller.conseller.core.auction.implement;

import com.conseller.conseller.core.auction.domain.Auction;
import com.conseller.conseller.core.auction.infrastructure.AuctionRepository;
import com.conseller.conseller.core.auction.infrastructure.AuctionRepositoryImpl;
import com.conseller.conseller.core.gifticon.implement.GifticonValidator;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuctionFinder {
    private final AuctionRepository auctionRepository;
    private final AuctionRepositoryImpl auctionRepositoryImpl;
    private final AuctionValidator auctionValidator;
    private final GifticonValidator gifticonValidator;

    public Auction findProgressAuction(long auctionIdx) {
        Auction auction = auctionRepository.findById(auctionIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.INVALID_AUCTION))
                .toDomain();

        auctionValidator.isInProgress(auction);
        gifticonValidator.isValidGiftion(auction.getGifticon());

        return auction;
    }

    public Auction findConfirmedBidAuction(long auctionIdx) {
        Auction auction = auctionRepository.findById(auctionIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.INVALID_AUCTION))
                .toDomain();

        auctionValidator.isConfirmedBid(auction);
        return auction;
    }

    public List<Auction> findAuctionsEndingWithinOneHour(long cursorId) {
        Auction cursor = auctionRepository.findById(cursorId)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.INVALID_AUCTION))
                .toDomain();

        return auctionRepositoryImpl.findAuctionsEndingWithinOneHour(cursor, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
    }
}
