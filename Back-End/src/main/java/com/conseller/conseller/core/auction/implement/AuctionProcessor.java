package com.conseller.conseller.core.auction.implement;

import com.conseller.conseller.core.Payment.implement.PaymentManager;
import com.conseller.conseller.core.auction.domain.Auction;
import com.conseller.conseller.core.auction.domain.AuctionOrder;
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

    private final AuctionOrderReader auctionOrderReader;
    private final AuctionOrderAppender auctionOrderAppender;

    private final UserReader userReader;
    private final UserFinder userFinder;

    private final BidReader bidReader;
    private final BidProcessor bidProcessor;

    private final PaymentManager paymentManager;

    private final SellerPaymentHistoryAppender sellerPaymentHistoryAppender;

    @Transactional
    public void buyNow(TargetAuction targetAuction, TargetBuyer targetBuyer) {
        Auction auction = auctionFinder.findProgressAuction(targetAuction.getAuctionIdx());
        User buyer = userReader.read(targetBuyer.getBuyerIdx());

        auctionManager.trade(auction, buyer);

        List<AuctionBid> auctionBids = bidReader.readAll(auction.getHighestBid().getAuctionBidIdx());
        bidProcessor.rejectAll(auctionBids);

        paymentManager.pay(buyer, auction.getUpperPrice());
        auctionOrderAppender.append(auction, buyer);
    }

    @Transactional
    public void buy(TargetAuction targetAuction, TargetBuyer targetBuyer) {
        Auction auction = auctionFinder.findProgressAuction(targetAuction.getAuctionIdx());
        User buyer = userFinder.findValidUser(targetBuyer.getBuyerIdx());

        auctionManager.trade(auction, buyer);

        List<AuctionBid> auctionBids = bidReader.readAll(auction.getHighestBid().getAuctionBidIdx());
        bidProcessor.rejectAll(auctionBids);

        paymentManager.pay(buyer, auction.getHighestBid().getAuctionBidPrice());
        auctionOrderAppender.append(auction, buyer);

    }

    @Transactional
    public void completeTrade(long auctionIdx, long buyerIdx) {
        // 유효한 상태인 경매 조회
        Auction auction = auctionFinder.findConfirmedBidAuction(auctionIdx);
        User buyer = userFinder.findValidUser(buyerIdx);

        // 구매자가 결제했던 이력을 조회
        AuctionOrder auctionOrder = auctionOrderReader.read(auction, buyer);

        // 판매자에게 대금 지급
        paymentManager.depositFondsToSeller(auction.getUser(), auctionOrder);

        // 판매 대금 지급 이력 생성 및 저장
        sellerPaymentHistoryAppender.append(auction, auction.getUser(), auctionOrder);
    }
}
