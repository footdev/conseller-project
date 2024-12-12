package com.conseller.conseller.core.auction.infrastructure;

import com.conseller.conseller.core.bid.infrastructure.AuctionBidEntity;
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
        List<Auction> auctionEntities = auctionService.getAuctionConfirmList();

        for(Auction auction : auctionEntities) {
            auctionService.confirmAuction(auction.getAuctionIdx());

            notificationService.sendAuctionNotification(auction.getAuctionIdx(),"경매 거래 확정", "님과의 거래가 자동으로 확정되었습니다.", 2,1);
        }
    }

    @Async
    @Scheduled(cron = "0 0 0 * * ?")
    public void autoAuctionExpire() {
        List<Auction> auctionEntities = auctionService.getAuctionExpiredList();

        for(Auction auction : auctionEntities) {
            List<AuctionBidEntity> bids = auction.getAuctionBidEntityList();
            for(AuctionBidEntity bid : bids) {
                auctionBidService.rejectAuctionBid(bid.getAuctionBidIdx());
            }
            auctionService.rejectAuction(auction.getAuctionIdx());
        }
    }

}
