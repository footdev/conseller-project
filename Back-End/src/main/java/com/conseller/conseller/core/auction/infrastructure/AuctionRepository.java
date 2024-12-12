package com.conseller.conseller.core.auction.infrastructure;

import com.conseller.conseller.core.auction.domain.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<AuctionEntity, Long> {

    @Query("select a from Auction a where a.auctionStatus = '거래 중' and current_timestamp - a.notificationCreatedDate >= 14 * 60 * 1000")
    List<AuctionEntity> findByAuctionListConfirm();

    @Query("select a from Auction a where a.auctionCompletedDate is null and a.auctionEndDate <= current_timestamp ")
    List<AuctionEntity> findAuctionAllExpired();

    @Query("select a from Auction a left join a.auctionBidList b where a.auctionStatus = '진행 중' group by a order by count(b) desc ")
    List<AuctionEntity> findAuctionList();

    @Query("select a from Auction a where a.auctionStatus = '낙찰'")
    List<AuctionEntity> findAwardedAuctionList();

    @Query("SELECT a FROM Auction a WHERE a.auctionEndDate BETWEEN :now AND :oneHourLater")
    List<Auction> findAuctionsEndingWithinOneHour(@Param("now") LocalDateTime now, @Param("oneHourLater") LocalDateTime oneHourLater);
}
