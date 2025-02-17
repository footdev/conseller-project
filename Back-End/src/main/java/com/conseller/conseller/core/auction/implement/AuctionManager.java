package com.conseller.conseller.core.auction.implement;

import com.conseller.conseller.core.auction.domain.Auction;
import com.conseller.conseller.core.auction.infrastructure.AuctionRepository;
import com.conseller.conseller.core.gifticon.implement.GifticonModifier;
import com.conseller.conseller.core.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuctionManager {
    private final AuctionRepository auctionRepository;
    private final GifticonModifier gifticonModifier;

    public void trade(Auction auction, User buyer) {
        auction.trade();
        auction.getHighestBidding().award();
        gifticonModifier.transfer(auction.getGifticon(), buyer);
    }
}
