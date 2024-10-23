package com.conseller.conseller.core.auction.implement;

import com.conseller.conseller.core.auction.infrastructure.AuctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuctionProcessor {
    private final AuctionRepository auctionRepository;


}
