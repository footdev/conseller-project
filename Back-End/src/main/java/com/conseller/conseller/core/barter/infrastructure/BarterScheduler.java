package com.conseller.conseller.core.barter.infrastructure;

import com.conseller.conseller.core.barter.domain.enums.BarterStatus;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterEntity;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterHostItemEntity;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterRequestEntity;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonEntity;
import com.conseller.conseller.core.gifticon.domain.enums.GifticonStatus;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonRepository;
import com.conseller.conseller.core.notification.domain.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BarterScheduler {
    private final BarterService barterService;
    private final BarterRequestService barterRequestService;
    private final BarterRepository barterRepository;
    private final GifticonRepository gifticonRepository;
    private final NotificationService notificationService;

    @Async
    @Transactional
    @Scheduled(cron="0 0 0 * * ?")
    public void autoBarterExpire() {
        List<BarterEntity> barterEntityEntities = barterService.getExpiredBarterList();
        for(BarterEntity barterEntity : barterEntityEntities) {
            List<BarterRequestEntity> barterRequestEntityList = barterEntity.getBarterRequestEntityList();
            for(BarterRequestEntity bq : barterRequestEntityList) {
                barterRequestService.rejectByTimeBarterRequest(bq.getBarterRequestIdx());
            }
            for(BarterHostItemEntity bhi : barterEntity.getBarterHostItemEntityList()) {
                GifticonEntity gifticonEntity = bhi.getGifticonEntity();
                gifticonEntity.setGifticonStatus(GifticonStatus.KEEP.getStatus());
                gifticonRepository.save(gifticonEntity);
            }
            barterEntity.setBarterCompletedDate(barterEntity.getBarterEndDate());
            barterEntity.setBarterStatus(BarterStatus.EXPIRED.getStatus());
            barterRepository.save(barterEntity);
            notificationService.sendBarterExpiredNotification(barterEntity.getBarterIdx(), "물물교환 만료 알림", 3);
            notificationService.sendBarterNotification(barterEntity.getBarterIdx(), "물물교환 알림", 3);
        }
    }
}
