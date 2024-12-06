package com.conseller.conseller.core.auction.implement;

import com.conseller.conseller.core.auction.domain.Auction;
import com.conseller.conseller.core.auction.infrastructure.AuctionEntity;
import com.conseller.conseller.core.auction.infrastructure.AuctionRepository;
import com.conseller.conseller.core.bid.domain.AuctionBid;
import com.conseller.conseller.core.bid.implement.BidReader;
import com.conseller.conseller.core.bid.implement.BidRemover;
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
    private final BidReader bidReader;
    private final BidRemover bidRemover;
    private final GifticonUpdater gifticonUpdater;
    private final AuctionReader  auctionReader;

    @Transactional
    public void remove(long id) {
        Auction auction = auctionReader.read(id);
        List<AuctionBid> auctionBids = bidReader.readAll(id);

        auction.delete();
        gifticonUpdater.updateStatus(auction.getGifticon(), GifticonStatus.KEEP);
        auctionRepository.save(AuctionEntity.of(auction));

        bidRemover.removeAll(auctionBids);

    }
}
