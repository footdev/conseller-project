package com.conseller.conseller.auction.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuctionTradeResponse {
    private String userName;
    private String userAccount;
    private String userAccountBank;
}
