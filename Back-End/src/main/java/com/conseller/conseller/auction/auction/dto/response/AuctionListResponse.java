package com.conseller.conseller.auction.auction.dto.response;

import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class AuctionListResponse {
    private List<AuctionItemData> items;
}
