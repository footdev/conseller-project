package com.conseller.conseller.core.bid.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuctionBidRepository extends JpaRepository<AuctionBidEntity, Long> {
    @Query("select ab from AuctionBid ab where ab.auction.auctionIdx = :auctionIdx order by ab.auctionBidPrice desc")
    List<AuctionBidEntity> findByAuctionIdxOrderByAuctionBidPriceDesc(Long auctionIdx);

    void deleteByUser_UserIdxAndAuction_AuctionIdx(Long userIdx, Long auctionIdx);

    Optional<AuctionBidEntity> findByUser_UserIdxAndAuction_AuctionIdx(Long userIdx, Long auctionIdx);
}
