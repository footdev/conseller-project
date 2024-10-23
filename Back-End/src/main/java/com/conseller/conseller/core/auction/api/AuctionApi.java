package com.conseller.conseller.core.auction.api;

import com.conseller.conseller.core.auction.api.dto.mapper.AuctionMapper;
import com.conseller.conseller.core.auction.api.dto.request.AuctionConfirmRequest;
import com.conseller.conseller.core.auction.api.dto.request.AuctionListRequest;
import com.conseller.conseller.core.auction.api.dto.request.ModifyAuctionRequest;
import com.conseller.conseller.core.auction.api.dto.request.RegistAuctionRequest;
import com.conseller.conseller.core.auction.api.dto.response.*;
import com.conseller.conseller.core.auction.domain.AuctionService;
import com.conseller.conseller.core.auction.infrastructure.AuctionEntity;
import com.conseller.conseller.core.notification.domain.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auction")
public class AuctionApi {
    private final AuctionService auctionService;
    private final NotificationService notificationService;

    // 경매 목록
    @PostMapping("/{auctionId}")
    public ResponseEntity<AuctionListResponse> getAuctionList(@PathVariable long auctionId, @RequestBody AuctionListRequest request) {
        AuctionListResponse response = auctionService.getAuctions(auctionId, request);
        return ResponseEntity.ok()
                .body(response);
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
        DetailAuctionResponse response =  auctionService.detailAuction(auctionIdx);
        return ResponseEntity.ok()
                .body(response);
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

    // 경매 거래 진행
    @GetMapping("/trade/{auctionIdx}/{userIdx}")
    public ResponseEntity<AuctionTradeResponse> tradeAuction(@PathVariable long auctionIdx, @PathVariable long userIdx) {
        AuctionTradeResponse response = auctionService.tradeAuction(auctionIdx, userIdx);
        notificationService.sendAuctionNotification(auctionIdx, "경매 거래 진행", "님과의 경매가 진행 중 입니다.", 1, 1);
        notificationService.sendAuctionNotification(auctionIdx, "경매 거래 진행", "님과의 경매가 진행 중 입니다.", 2, 1);
        return ResponseEntity.ok()
                .body(response);
    }

    // 경매 진행 취소
    // 없어도 될듯
    @PatchMapping("/cancel/{auctionIdx}")
    public ResponseEntity<Object> cancelAuction(@PathVariable long auctionIdx) {

        // 거래 취소 알림
        notificationService.sendAuctionNotification(auctionIdx, "경매 거래 취소", "님이 경매를 취소하였습니다", 1, 1);
        notificationService.sendAuctionNotification(auctionIdx, "경매 거래 취소", "님이 경매를 취소하였습니다", 2, 1);

        auctionService.cancelAuction(auctionIdx);

        return ResponseEntity.ok()
                .build();
    }

    // 입금 완료 버튼
    // 없어도 될듯
    @PatchMapping("/complete/{auctionIdx}")
    public ResponseEntity<Object> completeAuction(@PathVariable long auctionIdx) {
        // 판매자에게 알림
        notificationService.sendAuctionNotification(auctionIdx, "경매 입금 완료", "님이 경매 입금을 완료하였습니다.", 2, 1);

        return ResponseEntity.ok()
                .build();
    }

    // 거래 확정 버튼
    @PatchMapping("/confirm")
    public  ResponseEntity<Object> confirmAuction(@RequestBody AuctionConfirmRequest auctionConfirmRequest) {
        if(auctionConfirmRequest.getConfirm()) {
            auctionService.confirmAuction(auctionConfirmRequest.getAuctionIdx());

            notificationService.sendAuctionNotification(auctionConfirmRequest.getAuctionIdx(), "경매 거래 완료", "님과의 경매가 완료되었습니다.", 1, 1);
            notificationService.sendAuctionNotification(auctionConfirmRequest.getAuctionIdx(), "경매 거래 완료", "님과의 경매가 완료되었습니다.", 2, 1);
        }
        else {
            // 거래 취소 알림
            notificationService.sendAuctionNotification(auctionConfirmRequest.getAuctionIdx(), "경매 거래 취소", "님과의 경매가 취소되었습니다.", 1, 1);
            notificationService.sendAuctionNotification(auctionConfirmRequest.getAuctionIdx(), "경매 거래 취소", "님과의 경매가 취소되었습니다.", 2, 1);

            auctionService.cancelAuction(auctionConfirmRequest.getAuctionIdx());
        }

        return ResponseEntity.ok()
                .build();
    }

    // 경매 판매자 입금 확인
    @GetMapping("/Confirm/{auctionIdx}")
    public ResponseEntity<AuctionConfirmResponse> getConfirmAuction(@PathVariable long auctionIdx) {
        AuctionConfirmResponse response = auctionService.getConfirmAuction(auctionIdx);

        return ResponseEntity.ok()
                .body(response);
    }

    // 입금확인 페이지
    @GetMapping("/confirm-buy/{auctionIdx}")
    public ResponseEntity<AuctionConfirmBuyResponse> getConfirmBuyAuction(@PathVariable long auctionIdx) {
        AuctionConfirmBuyResponse response = auctionService.getConfirmBuyAuction(auctionIdx);

        return ResponseEntity.ok()
                .body(response);
    }

    // 가장 조회가 많이 된 경매
    @GetMapping("/popular")
    public ResponseEntity<AuctionPopularResponse> getPopularAuction() {
        List<AuctionEntity> auctionEntityList = auctionService.getPopularAuction();

        List<AuctionItemData> auctionItemDataList = AuctionMapper.INSTANCE.auctionsToItemDatas(auctionEntityList);

        AuctionPopularResponse response = new AuctionPopularResponse(auctionItemDataList);

        return ResponseEntity.ok()
                .body(response);
    }

    // 가장 많은 메인카테고리
    @GetMapping("/category/main")
    public ResponseEntity<AuctionCategoryResponse> getMainCategory() {
        List<Integer> list = auctionService.getMainCategory();

        AuctionCategoryResponse response = new AuctionCategoryResponse(list);

        return ResponseEntity.ok()
                .body(response);
    }

    // 가장 많은 서브카테고리
    @GetMapping("/category/sub")
    public ResponseEntity<AuctionCategoryResponse> getSubCategory() {

        List<Integer> list = auctionService.getSubCategory();

        AuctionCategoryResponse response = new AuctionCategoryResponse(list);

        return ResponseEntity.ok()
                .body(response);
    }

}
