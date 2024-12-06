package com.conseller.conseller.core.auction.implement;

import com.conseller.conseller.core.auction.domain.Auction;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import org.springframework.stereotype.Component;

@Component
public class AuctionValidator {

    public void isInProgress(Auction auction) {
        if (!auction.isInProgress()) {
            throw new CustomException(CustomExceptionStatus.ALREADY_TRADE_AUCTION);
        }
    }
}
