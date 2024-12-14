package com.conseller.conseller.core.auction.infrastructure;

import com.conseller.conseller.core.bid.infrastructure.BiddingEntity;
import com.conseller.conseller.core.notification.domain.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuctionScheduler {
    private final NotificationService notificationService;

    @Async
    @Scheduled(cron = "0 */15 * * * ?")
    public void autoAuctionConfirm() {
        List<AuctionEntity> auctionEntityEntities = auctionService.getAuctionConfirmList();

        for(AuctionEntity auctionEntity : auctionEntityEntities) {
            auctionService.confirmAuction(auctionEntity.getAuctionIdx());

            notificationService.sendAuctionNotification(auctionEntity.getAuctionIdx(),"경매 거래 확정", "님과의 거래가 자동으로 확정되었습니다.", 2,1);
        }
    }

    @Async
    @Scheduled(cron = "0 0 0 * * ?")
    public void autoAuctionExpire() {
        List<AuctionEntity> auctionEntityEntities = auctionService.getAuctionExpiredList();

        for(AuctionEntity auctionEntity : auctionEntityEntities) {
            List<BiddingEntity> bids = auctionEntity.getAuctionBidEntityList();
            for(BiddingEntity bid : bids) {
                auctionBidService.rejectAuctionBid(bid.getAuctionBidIdx());
            }
            auctionService.rejectAuction(auctionEntity.getAuctionIdx());
        }
    }

}
