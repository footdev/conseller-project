package com.conseller.conseller.core.auction.api.dto.response;

import com.conseller.conseller.core.auction.domain.Auction;
import lombok.*;

@Getter @Builder
public class AuctionItemData {
    private Long auctionIdx;
    private String gifticonDataImageName;
    private String gifticonName;
    private String gifticonEndDate;
    private String auctionEndDate;
    private String auctionStatus;
    private Boolean deposit;
    private Integer upperPrice;
    private Integer lowerPrice;
    private Integer auctionHighestBid;

    public static AuctionItemData of(Auction auction) {
        return AuctionItemData.builder()
                .auctionIdx(auction.getAuctionIdx())
                .gifticonDataImageName(auction.getGifticon().getGifticonName())
                .gifticonName(auction.getGifticon().getGifticonName())
                .gifticonEndDate(auction.getGifticon().getGifticonEndDate().toString())
                .auctionEndDate(auction.getAuctionEndDate().toString())
                .auctionStatus(auction.getAuctionStatus().getStatus())
                .deposit(auction)
                .upperPrice(auction.getUpperPrice())
                .lowerPrice(auction.getLowerPrice())
                .auctionHighestBid(auction.getHighestBid() == null ? 0 : auction.getHighestBid().getBidPrice())
                .build();
    }
}
