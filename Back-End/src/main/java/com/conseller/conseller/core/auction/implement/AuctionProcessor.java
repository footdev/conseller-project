package com.conseller.conseller.core.auction.implement;

import com.conseller.conseller.core.Payment.implement.PaymentManager;
import com.conseller.conseller.core.auction.domain.Auction;
import com.conseller.conseller.core.auction.domain.AuctionOrder;
import com.conseller.conseller.core.auction.domain.TargetAuction;
import com.conseller.conseller.core.auction.domain.TargetBuyer;
import com.conseller.conseller.core.bid.domain.Bidding;
import com.conseller.conseller.core.bid.implement.BiddingProcessor;
import com.conseller.conseller.core.bid.implement.BiddingReader;
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
    private final AuctionUpdater auctionUpdater;

    private final AuctionOrderReader auctionOrderReader;
    private final AuctionOrderAppender auctionOrderAppender;

    private final UserReader userReader;
    private final UserFinder userFinder;

    private final BiddingReader biddingReader;
    private final BiddingProcessor biddingProcessor;

    private final PaymentManager paymentManager;

    private final SellerPaymentHistoryAppender sellerPaymentHistoryAppender;

    @Transactional
    public void buyNow(TargetAuction targetAuction, TargetBuyer targetBuyer) {
        Auction auction = auctionFinder.findProgressAuction(targetAuction.getAuctionIdx());
        User buyer = userReader.read(targetBuyer.getBuyerIdx());

        auctionManager.trade(auction, buyer);

        List<Bidding> biddings = biddingReader.readAll(auction.getHighestBidding().getAuctionBidIdx());
        biddingProcessor.rejectAll(biddings);

        paymentManager.pay(buyer, auction.getUpperPrice());
        auctionOrderAppender.append(auction, buyer);
    }

    @Transactional
    public void buy(TargetAuction targetAuction, TargetBuyer targetBuyer) {
        Auction auction = auctionFinder.findProgressAuction(targetAuction.getAuctionIdx());
        User buyer = userFinder.findValidUser(targetBuyer.getBuyerIdx());

        auctionManager.trade(auction, buyer);

        List<Bidding> biddings = biddingReader.readAll(auction.getHighestBidding().getAuctionBidIdx());
        biddingProcessor.rejectAll(biddings);

        paymentManager.pay(buyer, auction.getHighestBidding().getBiddingPrice());
        auctionOrderAppender.append(auction, buyer);

    }

    @Transactional
    public void completeTrade(long auctionIdx, long buyerIdx) {
        Auction auction = auctionFinder.findConfirmedBidAuction(auctionIdx);
        User buyer = userFinder.findValidUser(buyerIdx);
        AuctionOrder auctionOrder = auctionOrderReader.read(auction, buyer);

        paymentManager.depositFondsToSeller(auction.getUser(), auctionOrder);
        sellerPaymentHistoryAppender.append(auction, auction.getUser(), auctionOrder);
    }

    @Transactional
    public void updateHighestBidding(Bidding bidding) {
        Auction auction = auctionFinder.findProgressAuction(bidding.getAuctionId());
        auction.updateHighestBid(bidding);
        auctionUpdater.updateAuction(auction);
    }
}
