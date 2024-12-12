package com.conseller.conseller.core.auction.domain;

import com.conseller.conseller.core.auction.api.dto.request.*;
import com.conseller.conseller.core.auction.api.dto.response.*;
import com.conseller.conseller.core.auction.implement.*;
import com.conseller.conseller.core.gifticon.domain.Gifticon;
import com.conseller.conseller.core.gifticon.implement.GifticonFinder;
import com.conseller.conseller.core.gifticon.implement.GifticonReader;
import com.conseller.conseller.core.gifticon.implement.GifticonUpdater;
import com.conseller.conseller.core.user.domain.User;
import com.conseller.conseller.core.user.implement.UserReader;
import com.conseller.conseller.core.gifticon.domain.enums.GifticonStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuctionService {
    private final AuctionReader auctionReader;
    private final AuctionFinder auctionFinder;
    private final AuctionAppender auctionAppender;
    private final AuctionUpdater auctionUpdater;
    private final AuctionRemover auctionRemover;
    private final AuctionProcessor auctionProcessor;

    private final UserReader userReader;
    private final GifticonFinder gifticonFinder;
    private final GifticonUpdater gifticonUpdater;

    public List<Auction> getAuctions(long id, AuctionListRequest request) {
        return auctionReader.read(id, request);
    }

    @Transactional
    public long registAuction(RegistAuctionRequest request) {
        User user = userReader.read(request.getUserIdx());
        Gifticon gifticon = gifticonFinder.findKeepGifticon(request.getGifticonIdx());

        gifticonUpdater.updateStatus(gifticon, GifticonStatus.AUCTION);
        return auctionAppender.append(Auction.of(request, user, gifticon));
    }

    public Auction detailAuction(long auctionIdx) {
        return auctionReader.read(auctionIdx);
    }

    public void modifyAuction(long id, ModifyAuctionRequest auctionRequest) {
        auctionUpdater.updateAuction(id, auctionRequest.getAuctionText());
    }

    public void deleteAuction(long id) {
        auctionRemover.remove(id);
    }

    // 낙찰 확정
    public void confirmWinningBid(TargetAuction targetAuction, TargetBuyer targetBuyer) {
        auctionProcessor.buy(targetAuction, targetBuyer);
    }

     //경매 거래 확정
    public void confirmAuction(long auctionIdx, long buyerIdx) {
        auctionProcessor.completeTrade(auctionIdx, buyerIdx);
    }

    // 하루 남은 경매 목록
    public List<Auction> getAuctionWithOneDayLeft() {
        return auctionFinder.findAuctionsEndingWithinOneHour();
    }

    // 즉시 구매 진행
    public void buyNowPrice(TargetAuction targetAuction, TargetBuyer targetBuyer) {
        auctionProcessor.buyNow(targetAuction, targetBuyer);
    }
}
