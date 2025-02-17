package com.conseller.conseller.core.auction.api.dto.response;

import com.conseller.conseller.core.auction.domain.Auction;
import com.conseller.conseller.core.bid.domain.Bidding;
import com.conseller.conseller.core.user.domain.User;
import lombok.*;

@Getter @Builder
public class AuctionItemData {
    private Long auctionIdx;
    private String gifticonDataImageName;
    private String gifticonName;
    private String gifticonEndDate;
    private String auctionEndDate;
    private String auctionStatus;
    private Integer upperPrice;
    private Integer lowerPrice;
    private User seller;
    private Bidding highestBid;

    public static AuctionItemData of(Auction auction) {
        return AuctionItemData.builder()
                .auctionIdx(auction.getAuctionIdx())
                .gifticonDataImageName(auction.getGifticon().getGifticonName())
                .gifticonName(auction.getGifticon().getGifticonName())
                .gifticonEndDate(auction.getGifticon().getGifticonEndDate().toString())
                .auctionEndDate(auction.getAuctionEndDate().toString())
                .auctionStatus(auction.getAuctionStatus().getStatus())
                .upperPrice(auction.getUpperPrice())
                .lowerPrice(auction.getLowerPrice())
                .seller(auction.getUser())
                .highestBid(auction.getHighestBidding())
                .build();
    }
}
