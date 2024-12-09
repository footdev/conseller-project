package com.conseller.conseller.core.auction.implement;

import com.conseller.conseller.core.auction.domain.Auction;
import com.conseller.conseller.core.auction.domain.AuctionOrder;
import com.conseller.conseller.core.auction.infrastructure.AuctionOrderRepository;
import com.conseller.conseller.core.user.domain.User;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuctionOrderReader {
    private final AuctionOrderRepository auctionOrderRepository;

    public AuctionOrder read(Auction auction, User buyer) {
        return auctionOrderRepository.findByAuctionIdxAndBuyerIdx(auction.getAuctionIdx(), buyer.getUserIdx())
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.AUCTION_ORDER_INVALID))
                .toDomain();
    }
}
