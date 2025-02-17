package com.conseller.conseller.core.bid.implement;

import com.conseller.conseller.core.auction.domain.Auction;
import com.conseller.conseller.core.bid.domain.Bidding;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import org.springframework.stereotype.Component;

@Component
public class BiddingValidator {
    public void validate(Bidding bidding, Auction auction) {
        if (bidding.getBiddingPrice() <= auction.getHighestBidding().getBiddingPrice()) {
            throw new CustomException(CustomExceptionStatus.INVALID_HIGHEST_BIDDING);
        }

        if (bidding.getBiddingPrice() > auction.getUpperPrice()) {
            throw new CustomException(CustomExceptionStatus.BID_EXCEEDS_UPPER_PRICE);
        }
    }
}
