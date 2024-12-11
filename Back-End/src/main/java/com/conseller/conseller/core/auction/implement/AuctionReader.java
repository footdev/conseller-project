package com.conseller.conseller.core.auction.implement;

import com.conseller.conseller.core.auction.api.dto.request.AuctionListRequest;
import com.conseller.conseller.core.auction.api.dto.response.AuctionItemData;
import com.conseller.conseller.core.auction.domain.Auction;
import com.conseller.conseller.core.auction.infrastructure.AuctionEntity;
import com.conseller.conseller.core.auction.infrastructure.AuctionRepository;
import com.conseller.conseller.core.auction.infrastructure.AuctionRepositoryImpl;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuctionReader {
    private final AuctionRepository auctionRepository;
    private final AuctionRepositoryImpl auctionRepositoryImpl;

    @Transactional(readOnly = true)
    public Auction read(long auctionIdx) {
        return auctionRepository.findById(auctionIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.AUCTION_INVALID))
                .toDomain();
    }

    @Transactional(readOnly = true)
    public List<Auction> read(long cursorId, AuctionListRequest auctionListRequest) {
        AuctionEntity cursor = auctionRepository.findById(cursorId)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.AUCTION_INVALID));

        return auctionRepositoryImpl.findAuctionListByCursor(cursor, auctionListRequest)
                .stream()
                .map(AuctionEntity::toDomain)
                .collect(Collectors.toList());
    }
}
