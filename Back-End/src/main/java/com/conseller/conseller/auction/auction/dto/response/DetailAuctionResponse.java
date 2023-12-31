package com.conseller.conseller.auction.auction.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DetailAuctionResponse {
    private String postContent;

    private Long auctionUserIdx;

    private String auctionUserNickname;

    private String auctionUserProfileUrl;

    private Long auctionUserDeposit;

    private List<AuctionBidItemData> auctionBidList;

    private Long auctionIdx;

    private String gifticonDataImageName;

    private String gifticonName;

    private String gifticonEndDate;

    private String auctionEndDate;

    private Boolean deposit;

    private Integer lowerPrice;

    private Integer upperPrice;

    private Integer auctionHighestBid;

}
