package com.conseller.conseller.core.barter.api;

import com.conseller.conseller.core.barter.api.dto.request.*;
import com.conseller.conseller.core.barter.api.dto.response.BarterConfirmPageResponse;
import com.conseller.conseller.core.barter.api.dto.response.BarterDetailResponse;
import com.conseller.conseller.core.barter.api.dto.response.BarterPagingResponse;
import com.conseller.conseller.core.barter.api.dto.response.CreateBarterResponse;
import com.conseller.conseller.core.barter.domain.BarterService;
import com.conseller.conseller.core.notification.domain.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/barter")
@RequiredArgsConstructor
public class BarterApi {

    private final BarterService barterService;
    private final NotificationService notificationService;

    @PostMapping("/all")
    public ResponseEntity<BarterPagingResponse> getAllBarterList(@RequestBody BarterFilterRequest barterFilterRequest) {
        return ResponseEntity.ok()
                .body(barterService.getBarterList(barterFilterRequest));
    }

    @GetMapping("/{barterIdx}/{userIdx}")
    public ResponseEntity<BarterDetailResponse> getBarter(@PathVariable Long barterIdx, @PathVariable Long userIdx) {
        return ResponseEntity.ok()
                .body(barterService.getBarter(barterIdx, userIdx));
    }

    //1. 물물교환 작성
    @PostMapping("/new")
    public ResponseEntity<CreateBarterResponse> addBarter(@RequestBody BarterCreateRequest barterCreateRequest) {
        CreateBarterResponse createBarterResponse = new CreateBarterResponse();
        createBarterResponse.setBarterIdx(barterService.addBarter(barterCreateRequest));
        return ResponseEntity.ok()
                .body(createBarterResponse);
    }

    //2. 물물교환 글 수정
    @PatchMapping("/{barterIdx}")
    public ResponseEntity<Void> modifyBarter(@PathVariable Long barterIdx, @RequestBody BarterModifyRequest barterModifyRequest) {
        barterService.modifyBarter(barterIdx, barterModifyRequest);
        return ResponseEntity.ok()
                .build();
    }

    @DeleteMapping("/{barterIdx}")
    public ResponseEntity<Void> deleteBarter(@PathVariable Long barterIdx) {
        barterService.deleteBarter(barterIdx);
        return ResponseEntity.ok()
                .build();
    }

    //4. 자신의 물물교환 신청글에 달린 물물 교환 신청에 대해 선택하기
    @PatchMapping("/Confirm")
    public ResponseEntity<Void> selectBarterRequest(@RequestBody BarterConfirmRequest barterConfirm) {

        if(barterConfirm.getConfirm()){
            barterService.exchangeGifticon(barterConfirm.getBarterIdx(), barterConfirm.getUserIdx());
            notificationService.sendBarterNotification(barterConfirm.getBarterIdx(), "물물교환 알림", 3);
        } else {
            barterService.rejectRequest(barterConfirm.getBarterIdx(), barterConfirm.getUserIdx());
        }

        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/Confirm/{barterIdx}")
    public ResponseEntity<BarterConfirmPageResponse> getConfirmPage(@PathVariable Long barterIdx) {
        return ResponseEntity.ok()
                .body(barterService.getBarterConfirmPage(barterIdx));
    }

    @GetMapping("/popular")
    public ResponseEntity<Object> getPopularBarter() {
        return ResponseEntity.ok()
                .body(barterService.getPopularBarter());
    }
}
