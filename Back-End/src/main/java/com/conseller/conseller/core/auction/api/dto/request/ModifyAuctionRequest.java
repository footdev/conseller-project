package com.conseller.conseller.core.auction.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class ModifyAuctionRequest {
    private String auctionText;
}
