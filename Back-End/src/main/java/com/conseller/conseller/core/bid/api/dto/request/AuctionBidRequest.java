package com.conseller.conseller.core.bid.api.dto.request;

import com.conseller.conseller.core.bid.domain.AddBidding;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuctionBidRequest {
    private long userIdx;
    private int auctionBidPrice;
    private LocalDateTime createdAt;

    public AddBidding toAddBidding() {
        return new AddBidding(userIdx, auctionBidPrice, createdAt);
    }
}
