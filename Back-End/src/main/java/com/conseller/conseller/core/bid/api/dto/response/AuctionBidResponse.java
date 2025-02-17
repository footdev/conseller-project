package com.conseller.conseller.core.bid.api.dto.response;

import com.conseller.conseller.core.auction.api.dto.response.AuctionItemData;
import lombok.*;

@Builder
@Getter @Setter @ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class AuctionBidResponse {

    private long auctionBidIdx;

    private int auctionBidPrice;

    private String auctionRegistedDate;

    private String auctionBidStatus;

    private AuctionItemData auctionItemData;
}
