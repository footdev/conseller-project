package com.conseller.conseller.core.auction.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuctionConfirmRequest {
    private Long auctionIdx;

    private Boolean confirm;
}
