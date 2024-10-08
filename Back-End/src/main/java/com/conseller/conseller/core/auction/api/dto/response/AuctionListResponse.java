package com.conseller.conseller.core.auction.api.dto.response;

import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class AuctionListResponse {
    private List<AuctionItemData> items;
}
