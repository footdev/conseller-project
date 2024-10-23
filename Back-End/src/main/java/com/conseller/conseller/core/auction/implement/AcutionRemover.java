package com.conseller.conseller.core.auction.implement;

import com.conseller.conseller.core.auction.domain.Auction;
import com.conseller.conseller.core.auction.infrastructure.AuctionEntity;
import com.conseller.conseller.core.auction.infrastructure.AuctionRepository;
import com.conseller.conseller.core.gifticon.implement.GifticonReader;
import com.conseller.conseller.core.gifticon.implement.GifticonUpdater;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonRepository;
import com.conseller.conseller.core.gifticon.infrastructure.enums.GifticonStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AcutionRemover {
    private final AuctionRepository auctionRepository;
    private final GifticonUpdater gifticonUpdater;
    private final AuctionReader  auctionReader;

    @Transactional
    public void remove(long id) {
        Auction auction = auctionReader.read(id);
        gifticonUpdater.updateStatus(auction.getGifticon(), GifticonStatus.KEEP);
        auctionRepository.delete(AuctionEntity.of(auction));
    }
}
