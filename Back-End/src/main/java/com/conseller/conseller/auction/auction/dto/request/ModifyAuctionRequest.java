package com.conseller.conseller.auction.auction.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModifyAuctionRequest {
    private String auctionEndDate;
    private String auctionText;
}