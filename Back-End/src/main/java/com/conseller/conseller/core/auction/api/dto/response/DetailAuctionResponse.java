package com.conseller.conseller.core.auction.api.dto.response;

import com.conseller.conseller.core.auction.domain.Auction;
import com.conseller.conseller.core.bid.domain.Bidding;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class DetailAuctionResponse {
    private String postContent;
    private long auctionUserIdx;
    private String auctionUserNickname;
    private String auctionUserProfileUrl;
    private long auctionUserDeposit;
    private long auctionIdx;
    private String gifticonDataImageName;
    private String gifticonName;
    private String gifticonEndDate;
    private String auctionEndDate;
    private boolean deposit;
    private int lowerPrice;
    private int upperPrice;
    private Bidding highestBid;

    public static DetailAuctionResponse of(Auction auction) {
        return DetailAuctionResponse.builder()
                .postContent(auction.getAuctionText())
                .auctionUserIdx(auction.getUser().getUserIdx())
                .auctionUserNickname(auction.getUser().getUserNickname())
                .auctionUserProfileUrl(auction.getUser().getUserProfileUrl())
                .auctionIdx(auction.getAuctionIdx())
                .gifticonDataImageName(auction.getGifticon().getGifticonDataImageUrl())
                .gifticonName(auction.getGifticon().getGifticonName())
                .gifticonEndDate(auction.getGifticon().getGifticonEndDate().toString())
                .auctionEndDate(auction.getAuctionEndDate().toString())
                .deposit(false)
                .lowerPrice(auction.getLowerPrice())
                .upperPrice(auction.getUpperPrice())
                .highestBid(auction.getHighestBidding())
                .build();
    }
}
