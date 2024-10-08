package com.conseller.conseller.core.auction.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuctionBidItemData {
    private Long auctionBidIdx;

    private Integer auctionBidPrice;

    private String auctionRegistedDate;

    private String auctionBidStatus;

    private Long userIdx;

    private Long auctionIdx;
}
