package com.conseller.conseller.core.bid.implement;

import com.conseller.conseller.core.auction.domain.Auction;
import com.conseller.conseller.core.auction.implement.AuctionFinder;
import com.conseller.conseller.core.auction.implement.AuctionProcessor;
import com.conseller.conseller.core.bid.domain.AddBidding;
import com.conseller.conseller.core.bid.domain.Bidding;
import com.conseller.conseller.core.user.domain.User;
import com.conseller.conseller.core.user.implement.UserFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BiddingProcessor {

    private final AuctionFinder auctionFinder;
    private final UserFinder userFinder;
    private final BiddingValidator biddingValidator;
    private final AuctionProcessor auctionProcessor;

    public void rejectAll(List<Bidding> biddings) {
        biddings.forEach(Bidding::reject);
    }

    @Transactional
    public void bid(long auctionIdx, AddBidding addBidding) {
        Auction auction = auctionFinder.findProgressAuction(auctionIdx);
        User user = userFinder.findValidUser(addBidding.getBidUserId());

        Bidding bidding = Bidding.of(auction, user, addBidding);
        biddingValidator.validate(bidding, auction);

        auctionProcessor.updateHighestBidding(bidding);
    }
}
