package com.conseller.conseller.auction.infrastructure;

import com.conseller.conseller.auction.domain.AuctionService;
import com.conseller.conseller.bid.domain.AuctionBidService;
import com.conseller.conseller.entity.AuctionBid;
import com.conseller.conseller.notification.domain.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuctionScheduler {
    private final AuctionService auctionService;
    private final AuctionBidService auctionBidService;
    private final NotificationService notificationService;

    @Async
    @Scheduled(cron = "0 */15 * * * ?")
    public void autoAuctionConfirm() {
        List<AuctionEntity> auctionEntities = auctionService.getAuctionConfirmList();

        for(AuctionEntity auctionEntity : auctionEntities) {
            auctionService.confirmAuction(auctionEntity.getAuctionIdx());

            notificationService.sendAuctionNotification(auctionEntity.getAuctionIdx(),"경매 거래 확정", "님과의 거래가 자동으로 확정되었습니다.", 2,1);
        }
    }

    @Async
    @Scheduled(cron = "0 0 0 * * ?")
    public void autoAuctionExpire() {
        List<AuctionEntity> auctionEntities = auctionService.getAuctionExpiredList();

        for(AuctionEntity auctionEntity : auctionEntities) {
            List<AuctionBid> bids = auctionEntity.getAuctionBidList();
            for(AuctionBid bid : bids) {
                auctionBidService.rejectAuctionBid(bid.getAuctionBidIdx());
            }
            auctionService.rejectAuction(auctionEntity.getAuctionIdx());
        }
    }

}
