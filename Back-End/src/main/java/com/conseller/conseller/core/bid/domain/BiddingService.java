package com.conseller.conseller.core.bid.domain;

import com.conseller.conseller.core.auction.implement.AuctionFinder;
import com.conseller.conseller.core.bid.implement.BiddingProcessor;
import com.conseller.conseller.core.bid.implement.BiddingRemover;
import com.conseller.conseller.core.bid.implement.BiddingValidator;
import com.conseller.conseller.core.user.implement.UserFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BiddingService {

    private final AuctionFinder auctionFinder;
    private final UserFinder userFinder;
    private final BiddingValidator biddingValidator;
    private final BiddingProcessor biddingProcessor;
    private final BiddingRemover biddingRemover;

    public void bid(long auctionIdx, AddBidding addBidding) {
        biddingProcessor.bid(auctionIdx, addBidding);
    }

    public void cancel(TargetBidding targetBidding) {
        biddingRemover.cancel(targetBidding);
    }
}
