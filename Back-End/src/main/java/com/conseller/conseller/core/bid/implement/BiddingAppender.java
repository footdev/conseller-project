package com.conseller.conseller.core.bid.implement;

import com.conseller.conseller.core.auction.domain.Auction;
import com.conseller.conseller.core.bid.domain.Bidding;
import com.conseller.conseller.core.bid.infrastructure.BiddingEntity;
import com.conseller.conseller.core.bid.infrastructure.BiddingRepository;
import com.conseller.conseller.core.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BiddingAppender {
    private final BiddingRepository biddingRepository;

    public void append(Bidding bidding) {
        biddingRepository.save(BiddingEntity.of(bidding));
    }
}
