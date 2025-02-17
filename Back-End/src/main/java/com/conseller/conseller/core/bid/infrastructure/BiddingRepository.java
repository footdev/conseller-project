package com.conseller.conseller.core.bid.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BiddingRepository extends JpaRepository<BiddingEntity, Long> {
    @Query("SELECT ab FROM AuctionBid ab " +
            "WHERE ab.auction.auctionIdx = :auctionIdx " +
            "AND (" +
            "  SELECT COUNT(DISTINCT ab2.auctionBidPrice) " +
            "  FROM AuctionBid ab2 " +
            "  WHERE ab2.auction.auctionIdx = :auctionIdx " +
            "  AND ab2.auctionBidPrice > ab.auctionBidPrice" +
            ") = 1")
    BiddingEntity findSecondHighestBidByAuctionIdx(Long auctionIdx);

    void deleteByUser_UserIdxAndAuction_AuctionIdx(Long userIdx, Long auctionIdx);

    Optional<BiddingEntity> findByUser_UserIdxAndAuction_AuctionIdx(Long userIdx, Long auctionIdx);

    List<BiddingEntity> findAllByAuctionIdx(Long auctionIdx);
}
