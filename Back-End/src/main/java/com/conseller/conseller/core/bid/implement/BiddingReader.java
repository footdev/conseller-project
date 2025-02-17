package com.conseller.conseller.core.bid.implement;

import com.conseller.conseller.core.bid.domain.Bidding;
import com.conseller.conseller.core.bid.infrastructure.BiddingEntity;
import com.conseller.conseller.core.bid.infrastructure.BiddingRepository;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BiddingReader {
    private final BiddingRepository biddingRepository;

    public Bidding read(long auctionBidIdx) {
        return biddingRepository.findById(auctionBidIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.NOT_EXIST_BID))
                .toDomain();
    }

    public List<Bidding> readAll(Long auctionIdx) {
        return biddingRepository.findAllByAuctionIdx(auctionIdx)
                .stream()
                .map(BiddingEntity::toDomain)
                .collect(Collectors.toList());
    }
}
