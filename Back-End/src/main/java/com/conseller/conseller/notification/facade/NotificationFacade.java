package com.conseller.conseller.notification.facade;

import com.conseller.conseller.gifticon.dto.response.ExpiringGifticonResponse;
import com.conseller.conseller.gifticon.service.GifticonService;
import com.conseller.conseller.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.util.List;

@Component
@Log4j2
@RequiredArgsConstructor
public class NotificationFacade {

    private final NotificationService notificationService;
    private final GifticonService gifticonService;
    private final StopWatch stopWatch;

    @Transactional
    public String sendGifticonExpirationDateNotification() {

        log.info("기프티콘 유효기간 알림 전송 시작");
        stopWatch.start();

        List<ExpiringGifticonResponse> gifticonExpirationDates = gifticonService.getGifticonExpirationDates();

        gifticonExpirationDates.forEach(
                expirationDate -> notificationService.sendGifticonNotification(
                        expirationDate.getUserIdx(),
                        expirationDate.getExpiryDay(),
                        expirationDate.getGifticonName(),
                        expirationDate.getGifticonCnt(),
                        1
                )
        );

        stopWatch.stop();
        return "실행시간 " + stopWatch.getTotalTimeMillis() + "ms";
    }
}
