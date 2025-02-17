package com.conseller.conseller.core.auction.api.dto.response;

import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AuctionPopularResponse {
    private List<AuctionItemData> items;
}
