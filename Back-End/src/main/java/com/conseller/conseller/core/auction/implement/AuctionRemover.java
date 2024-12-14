package com.conseller.conseller.core.auction.implement;

import com.conseller.conseller.core.auction.domain.Auction;
import com.conseller.conseller.core.auction.infrastructure.AuctionEntity;
import com.conseller.conseller.core.auction.infrastructure.AuctionRepository;
import com.conseller.conseller.core.bid.domain.Bidding;
import com.conseller.conseller.core.bid.implement.BiddingReader;
import com.conseller.conseller.core.bid.implement.BiddingRemover;
import com.conseller.conseller.core.gifticon.implement.GifticonUpdater;
import com.conseller.conseller.core.gifticon.domain.enums.GifticonStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuctionRemover {
    private final AuctionRepository auctionRepository;
    private final BiddingReader biddingReader;
    private final BiddingRemover biddingRemover;
    private final GifticonUpdater gifticonUpdater;
    private final AuctionReader  auctionReader;

    @Transactional
    public void remove(long auctionId) {
        Auction auction = auctionReader.read(auctionId);
        List<Bidding> biddings = biddingReader.readAll(auctionId);

        auction.delete();
        gifticonUpdater.updateToKeepStatus(auction.getGifticon());
        auctionRepository.save(AuctionEntity.of(auction));

        biddingRemover.removeAll(biddings);
    }
}
