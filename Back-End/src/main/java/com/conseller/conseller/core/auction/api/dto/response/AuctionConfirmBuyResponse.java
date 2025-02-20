package com.conseller.conseller.core.auction.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuctionConfirmBuyResponse {
    private String gifticonDataImageName;

    private String giftconName;

    private Integer auctionPrice;

    private String postContent;

    private String userName;

    private String userAccount;

    private String userAccountBank;

    private String buyUserImageUrl;

    private String buyUserNickname;

    private String buyUserName;

    private Long buyUserIdx;
}
