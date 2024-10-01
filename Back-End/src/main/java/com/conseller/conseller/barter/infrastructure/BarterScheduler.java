package com.conseller.conseller.barter.infrastructure;

import com.conseller.conseller.barter.domain.BarterService;
import com.conseller.conseller.barter.domain.enums.BarterStatus;
import com.conseller.conseller.barter.domain.BarterRequestService;
import com.conseller.conseller.entity.Gifticon;
import com.conseller.conseller.gifticon.domain.enums.GifticonStatus;
import com.conseller.conseller.gifticon.infrastructure.GifticonRepository;
import com.conseller.conseller.notification.domain.NotificationService;
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
        List<Barter> barters = barterService.getExpiredBarterList();
        for(Barter barter : barters) {
            List<BarterRequest> barterRequestList = barter.getBarterRequestList();
            for(BarterRequest bq : barterRequestList) {
                barterRequestService.rejectByTimeBarterRequest(bq.getBarterRequestIdx());
            }
            for(BarterHostItem bhi : barter.getBarterHostItemList()) {
                Gifticon gifticon = bhi.getGifticon();
                gifticon.setGifticonStatus(GifticonStatus.KEEP.getStatus());
                gifticonRepository.save(gifticon);
            }
            barter.setBarterCompletedDate(barter.getBarterEndDate());
            barter.setBarterStatus(BarterStatus.EXPIRED.getStatus());
            barterRepository.save(barter);
            notificationService.sendBarterExpiredNotification(barter.getBarterIdx(), "물물교환 만료 알림", 3);
            notificationService.sendBarterNotification(barter.getBarterIdx(), "물물교환 알림", 3);
        }
    }
}
