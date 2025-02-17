package com.conseller.conseller.core.auction.api.dto.response;

import com.conseller.conseller.core.auction.domain.Auction;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class AuctionListResponse {
    private List<AuctionItemData> items;

    public static AuctionListResponse of(List<Auction> auctions) {
        return new AuctionListResponse(
            auctions.stream()
                    .map(AuctionItemData::of)
                    .collect(Collectors.toList())
        );
    }
}
