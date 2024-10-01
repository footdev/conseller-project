package com.conseller.conseller.bid.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuctionBidRequest {
    private Long userIdx;
    private Integer auctionBidPrice;
}
