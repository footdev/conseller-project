package com.conseller.conseller.core.store.infrastructure;

import com.conseller.conseller.core.notification.domain.NotificationService;
import com.conseller.conseller.core.store.domain.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StoreScheduler {
    private final StoreService storeService;
    private final NotificationService notificationService;

    @Async
    @Scheduled(cron = "0 */15 * * * ?")
    public void autoStoreConfirm() {
        List<StoreEntity> storeEntities = storeService.getStoreConfirmList();

        for(StoreEntity storeEntity : storeEntities) {
            storeService.confirmStore(storeEntity.getStoreIdx());

            notificationService.sendStoreNotification(storeEntity.getStoreIdx(),"스토어 거래 확정", "님과의 거래가 자동으로 확정되었습니다.", 2,2);
        }
    }

    @Async
    @Scheduled(cron = "0 0 0 * * ?")
    public void autoStoreExpire() {
        List<StoreEntity> storeEntities = storeService.getStoreExpiredList();

        for(StoreEntity storeEntity : storeEntities) {
            storeService.rejectStore(storeEntity.getStoreIdx());
        }
    }
}
