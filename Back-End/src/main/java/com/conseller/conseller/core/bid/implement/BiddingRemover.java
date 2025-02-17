package com.conseller.conseller.core.bid.implement;

import com.conseller.conseller.core.auction.domain.Auction;
import com.conseller.conseller.core.auction.implement.AuctionFinder;
import com.conseller.conseller.core.auction.implement.AuctionProcessor;
import com.conseller.conseller.core.bid.domain.Bidding;
import com.conseller.conseller.core.bid.domain.TargetBidding;
import com.conseller.conseller.core.bid.infrastructure.BiddingEntity;
import com.conseller.conseller.core.bid.infrastructure.BiddingRepository;
import com.conseller.conseller.core.user.domain.User;
import com.conseller.conseller.core.user.implement.UserFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BiddingRemover {
    private final BiddingRepository biddingRepository;
    private final UserFinder userFinder;
    private final AuctionFinder auctionFinder;
    private BiddingReader biddingReader;
    private final AuctionProcessor auctionProcessor;

    public void removeAll(List<Bidding> biddings) {
        biddings.forEach(Bidding::delete);
        biddingRepository.saveAll(
                biddings.stream()
                        .map(BiddingEntity::of)
                        .collect(Collectors.toList())
        );
    }

    @Transactional
    public void cancel(TargetBidding targetBidding) {
        User bidUser = userFinder.findValidUser(targetBidding.getBidUserId());
        Bidding bidding = biddingReader.read(targetBidding.getBidUserId());
        Auction auction = auctionFinder.findProgressAuction(bidding.getAuctionId());

        if (bidUser.getUserIdx() == auction.getHighestBidding().getUserId()) {
            Bidding secondHighestBidding = biddingRepository.findSecondHighestBidByAuctionIdx(auction.getAuctionIdx()).toDomain();
            auctionProcessor.updateHighestBidding(secondHighestBidding);
            return;
        }

        bidding.delete();
        biddingRepository.save(BiddingEntity.of(bidding));
    }
}
