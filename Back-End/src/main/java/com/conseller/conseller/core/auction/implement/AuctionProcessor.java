package com.conseller.conseller.core.auction.implement;

import com.conseller.conseller.core.Payment.implement.PaymentManager;
import com.conseller.conseller.core.auction.domain.Auction;
import com.conseller.conseller.core.auction.domain.TargetAuction;
import com.conseller.conseller.core.auction.domain.TargetBuyer;
import com.conseller.conseller.core.bid.domain.AuctionBid;
import com.conseller.conseller.core.bid.implement.BidProcessor;
import com.conseller.conseller.core.bid.implement.BidReader;
import com.conseller.conseller.core.user.domain.User;
import com.conseller.conseller.core.user.implement.UserFinder;
import com.conseller.conseller.core.user.implement.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuctionProcessor {
    private final AuctionFinder auctionFinder;
    private final AuctionManager auctionManager;

    private final AuctionOrderAppender auctionOrderAppender;

    private final UserReader userReader;
    private final UserFinder userFinder;

    private final BidReader bidReader;
    private final BidProcessor bidProcessor;

    private final PaymentManager paymentManager;

    @Transactional
    public void buyNow(TargetAuction targetAuction, TargetBuyer targetBuyer) {
        Auction auction = auctionFinder.findValidAuction(targetAuction.getAuctionIdx());
        User buyer = userReader.read(targetBuyer.getBuyerIdx());

        auctionManager.trade(auction, buyer);

        List<AuctionBid> auctionBids = bidReader.readAll(auction.getHighestBid().getAuctionBidIdx());
        bidProcessor.rejectAll(auctionBids);

        paymentManager.pay(buyer, auction.getUpperPrice());
        auctionOrderAppender.append(auction, buyer);
    }

    public void buy(TargetAuction targetAuction, TargetBuyer targetBuyer) {
        Auction auction = auctionFinder.findValidAuction(targetAuction.getAuctionIdx());
        User buyer = userFinder.findValidUser(targetBuyer.getBuyerIdx());

        auctionManager.trade(auction, buyer);

        List<AuctionBid> auctionBids = bidReader.readAll(auction.getHighestBid().getAuctionBidIdx());
        bidProcessor.rejectAll(auctionBids);

        paymentManager.pay(buyer, auction.getHighestBid().getAuctionBidPrice());
        auctionOrderAppender.append(auction, buyer);
    }
}
