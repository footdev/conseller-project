package com.conseller.conseller.core.auction.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class AuctionBidItemData {
    private long auctionBidIdx;
    private int auctionBidPrice;
    private String auctionRegistedDate;
    private String auctionBidStatus;
    private long userIdx;
    private long auctionIdx;
}
