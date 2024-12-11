package com.conseller.conseller.core.auction.api;

import com.conseller.conseller.core.auction.api.dto.request.*;
import com.conseller.conseller.core.auction.api.dto.response.*;
import com.conseller.conseller.core.auction.domain.Auction;
import com.conseller.conseller.core.auction.domain.AuctionService;
import com.conseller.conseller.core.notification.domain.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auction")
public class AuctionApi {
    private final AuctionService auctionService;
    private final NotificationService notificationService;

    // 경매 목록
    @PostMapping("/{auctionId}")
    public ResponseEntity<AuctionListResponse> getAuctionList(@PathVariable long auctionId, @RequestBody AuctionListRequest request) {
        return ResponseEntity.ok()
                .body(new AuctionListResponse(auctionService.getAuctions(auctionId, request)));
    }

    // 경매 글 등록
    @PostMapping("/regist")
    public ResponseEntity<RegistAuctionResponse> registAuction(@RequestBody RegistAuctionRequest request) {
        Long auctionIdx = auctionService.registAuction(request);
        RegistAuctionResponse response = new RegistAuctionResponse(auctionIdx);
        return ResponseEntity.ok()
                .body(response);
    }

    // 경매 글 상세 보기
    @GetMapping("/{auctionIdx}")
    public ResponseEntity<DetailAuctionResponse> detailAuction(@PathVariable long auctionIdx) {
        return ResponseEntity.ok()
                .body(DetailAuctionResponse.of(auctionService.detailAuction(auctionIdx)));
    }

    // 경매 글 수정
    @PatchMapping("/{auctionIdx}")
    public ResponseEntity<Object> modifyAuction(@PathVariable long auctionIdx, @RequestBody ModifyAuctionRequest auctionRequest) {
        auctionService.modifyAuction(auctionIdx, auctionRequest);
        return ResponseEntity.ok()
                .build();
    }

    // 경매 글 삭제
    @DeleteMapping("/{auctionIdx}")
    public ResponseEntity<Object> deleteAuction(@PathVariable long auctionIdx) {
        auctionService.deleteAuction(auctionIdx);
        return ResponseEntity.ok()
                .build();
    }

    // 즉시 구매가 거래 진행
    @PostMapping("/buy-now")
    public ResponseEntity<Object> buyNowPrice(@RequestBody BuyNowRequest buyNowRequest) {
        auctionService.buyNowPrice(buyNowRequest.toTargetAuction(), buyNowRequest.toTargetBuyer());
        return ResponseEntity.ok()
                .build();

    }

    // 낙찰자 입장에서 낙찰 확정
    @PostMapping("/confirm-bid")
    public ResponseEntity<Object> confirmBid(@RequestBody ConfirmBidRequest confirmBidRequest) {
        auctionService.confirmWinningBid(confirmBidRequest.toTargetAuction(), confirmBidRequest.toTargetBuyer());
        return ResponseEntity.ok()
                .build();
    }

    // 낙찰자가 기프티콘 사용 후 거래 완전 확정
    @PostMapping("/confirm-trade")
    public ResponseEntity<Object> confirmTrade(@RequestBody ConfirmTradeRequest confirmTradeRequest) {
        auctionService.confirmAuction(confirmTradeRequest.getAuctionIdx(), confirmTradeRequest.getBuyerIdx());
        return ResponseEntity.ok()
                .build();
    }

    // 경매 거래 진행
    @PostMapping("/trade/{auctionIdx}/{userIdx}")
    public ResponseEntity<AuctionTradeResponse> tradeAuction(@PathVariable long auctionIdx, @PathVariable long userIdx) {
        AuctionTradeResponse response = auctionService.tradeAuction(auctionIdx, userIdx);
        notificationService.sendAuctionNotification(auctionIdx, "경매 거래 진행", "님과의 경매가 진행 중 입니다.", 1, 1);
        notificationService.sendAuctionNotification(auctionIdx, "경매 거래 진행", "님과의 경매가 진행 중 입니다.", 2, 1);
        return ResponseEntity.ok()
                .body(response);
    }

    // 하루 남은 경매 리스트
    @GetMapping("/popular")
    public ResponseEntity<AuctionPopularResponse> getPopularAuction() {
        List<Auction> popularAuctions = auctionService.getAuctionWithOneDayLeft();
        return ResponseEntity.ok()
                .body(new AuctionPopularResponse(
                        popularAuctions.stream()
                                .map(AuctionItemData::of)
                                .collect(Collectors.toList())
                ));
    }
}
