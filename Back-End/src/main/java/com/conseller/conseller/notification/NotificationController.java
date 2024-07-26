package com.conseller.conseller.notification;

import com.conseller.conseller.notification.dto.request.NotificationAnswerRequest;
import com.conseller.conseller.notification.dto.response.NotificationListResponse;
import com.conseller.conseller.notification.facade.NotificationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {
    private final NotificationService notificationService;
    private final NotificationFacade notificationFacade;

    // 알림 목록
    @GetMapping("/{user_idx}")
    public ResponseEntity<NotificationListResponse> getNotificationList(@PathVariable("user_idx") Long userIdx) {

        NotificationListResponse response = notificationService.getNotificationList(userIdx);

        return ResponseEntity.ok()
                .body(response);
    }

    // 알림 응답
    @PatchMapping("/{user_idx}")
    public ResponseEntity<Object> getAnswer(@PathVariable("user_idx") Long userIdx, @RequestBody NotificationAnswerRequest request) {
        notificationService.getAnswer(userIdx, request);

        return ResponseEntity.ok()
                .build();
    }

    // 기프티콘 알람 응답
    @GetMapping("/expiration-date")
    public ResponseEntity<String> sendExpirationDateNotification() {
        String ms = notificationFacade.sendGifticonExpirationDateNotification();
        return ResponseEntity.ok()
                .body(ms);
    }
}
