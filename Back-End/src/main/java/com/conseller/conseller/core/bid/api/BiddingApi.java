package com.conseller.conseller.core.bid.api;

import com.conseller.conseller.core.bid.api.dto.request.AuctionBidRequest;
import com.conseller.conseller.core.bid.domain.BiddingService;
import com.conseller.conseller.core.bid.domain.TargetBidding;
import com.conseller.conseller.core.notification.domain.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BiddingApi {

    private final BiddingService biddingService;
    private final NotificationService notificationService;

    // 입찰
    @PostMapping("/bid/{auction_idx}")
    public ResponseEntity<Object> registBidding(@PathVariable("auction_idx") long auctionIdx, @RequestBody AuctionBidRequest request) {
        biddingService.bid(auctionIdx, request.toAddBidding());
        notificationService.sendAuctionBidNotification(auctionIdx, request.getUserIdx(), "경매 입찰 알림", 1);
        return ResponseEntity.ok()
                .build();
    }

    // 입찰 취소
    @DeleteMapping("/bid")
    public ResponseEntity<Object> cancleBidding(@RequestBody TargetBidding targetBidding) {
        biddingService.cancel(targetBidding);

        return ResponseEntity.ok()
                .build();
    }

}
