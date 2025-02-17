package com.conseller.conseller.core.barter.api;

import com.conseller.conseller.core.barter.api.dto.request.*;
import com.conseller.conseller.core.barter.api.dto.response.BarterRequestsResponse;
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
                .body(barterService.getBarters(barterFilterRequest));
    }

    @GetMapping("/{barterIdx}/{userIdx}")
    public ResponseEntity<BarterDetailResponse> getBarter(@PathVariable Long barterIdx, @PathVariable Long userIdx) {
        return ResponseEntity.ok()
                .body(barterService.getBarter(barterIdx, userIdx));
    }

    @PostMapping("/new")
    public ResponseEntity<CreateBarterResponse> addBarter(@RequestBody BarterCreateRequest barterCreateRequest) {
        CreateBarterResponse createBarterResponse = new CreateBarterResponse();
        createBarterResponse.setBarterIdx(barterService.createBarter(barterCreateRequest));
        return ResponseEntity.ok()
                .body(createBarterResponse);
    }

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

    @PatchMapping("/accept")
    public ResponseEntity<Void> selectBarterRequest(@RequestBody BarterConfirmRequest barterConfirm) {
        barterService.exchangeGifticon(barterConfirm.getBarterIdx(), barterConfirm.getUserIdx(), barterConfirm.getBarterRequestIdx());
        notificationService.sendBarterNotification(barterConfirm.getBarterIdx(), "물물교환 알림", 3);
        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/requests/{barterIdx}")
    public ResponseEntity<BarterRequestsResponse> getBarterRequests(@PathVariable Long barterIdx) {
        return ResponseEntity.ok()
                .body(barterService.getBarterRequests(barterIdx));
    }
}
