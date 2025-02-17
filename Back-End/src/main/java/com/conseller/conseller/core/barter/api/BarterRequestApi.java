package com.conseller.conseller.core.barter.api;

import com.conseller.conseller.core.barter.api.dto.request.BarterRegistRequest;
import com.conseller.conseller.core.barter.api.dto.response.BarterRequestResponse;
import com.conseller.conseller.core.barter.api.dto.response.BarterRequestsResponse;
import com.conseller.conseller.core.barter.domain.BarterRequestService;
import com.conseller.conseller.core.notification.domain.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/barterRequests")
public class BarterRequestApi {

    private final BarterRequestService barterRequestService;
    private final NotificationService notificationService;

    // 공통
    // 물물교환 요청 상세 조회
    @GetMapping("/{barterRequestIdx}")
    public ResponseEntity<BarterRequestResponse> getBarterRequest(@PathVariable Long barterRequestIdx) {
        return ResponseEntity.ok()
                .body(barterRequestService.getBarterRequest(barterRequestIdx));
    }

    // 호스트 입장
    // 물물교환 글에 대한 모든 요청 조회
    @GetMapping("{barterIdx}")
    public ResponseEntity<BarterRequestsResponse> getBarterRequestsByBarter(@PathVariable Long barterIdx) {
        return ResponseEntity.ok()
                .body(barterRequestService.getBarterRequests(barterIdx));
    }

    // 구매자 입장
    @PostMapping("/{barterIdx}")
    public ResponseEntity<Void> addBarterRequest(@PathVariable Long barterIdx, @RequestBody BarterRegistRequest barterRegistRequest){
        barterRequestService.addBarterRequest(barterRegistRequest, barterIdx);
        notificationService.sendBarterRequestNotification(barterIdx, "물물교환 신청 알림", 3);
        return ResponseEntity.ok()
                .build();
    }

    @DeleteMapping("/{barterRequestIdx}")
    public ResponseEntity<Void> deleteBarterRequest(@PathVariable Long barterRequestIdx) {
        barterRequestService.deleteBarterRequest(barterRequestIdx);
        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/guest/{userIdx}")
    public ResponseEntity<List<BarterRequestResponse>> getBarterRequestsByUser(@PathVariable Long userIdx){
        return ResponseEntity.ok()
                .body(barterRequestService.getBarterRequestsByUser(userIdx));
    }


}
