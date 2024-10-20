package com.conseller.conseller.core.notification.facade;

import com.conseller.conseller.core.gifticon.domain.GifticonService;
import com.conseller.conseller.core.notification.infrastructure.NotificationEntity;
import com.conseller.conseller.global.exception.CustomException;

import com.conseller.conseller.core.gifticon.api.dto.response.ExpiringGifticonResponse;

import com.conseller.conseller.core.notification.domain.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
@Log4j2
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationFacade {

    private final NotificationService notificationService;
    private final GifticonService gifticonService;
    private final StopWatch stopWatch;

    private final int processors = Runtime.getRuntime().availableProcessors();
    private final int threadPoolSize = Math.max(2, processors);
    private final ExecutorService customThreadPool = Executors.newWorkStealingPool(threadPoolSize);

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public String sendGifticonExpirationDateNotification() {

        log.info("기프티콘 유효기간 알림 전송 시작");
        stopWatch.start();

        List<ExpiringGifticonResponse> gifticonExpirationDates = gifticonService.getGifticonExpirationDates();

        // 비동기 작업 생성 및 실행
        List<CompletableFuture<NotificationEntity>> futures = gifticonExpirationDates.stream()
                .map(expiringGifticonResponse -> CompletableFuture.supplyAsync(
                        () -> notificationService.createGifticonNotification(
                                expiringGifticonResponse.getUserIdx(),
                                expiringGifticonResponse.getExpiryDay(),
                                expiringGifticonResponse.getGifticonName(),
                                expiringGifticonResponse.getGifticonCnt(),
                                1
                        ), customThreadPool).exceptionally(
                                e -> {
                                    log.error("알람 일괄 처리 도중 에러가 발생했습니다.");
                                    return null;
                                }
                        )
                )
                .collect(Collectors.toList());

        // 모든 비동기 작업이 완료될 때까지 대기하고 결과 수집
        List<NotificationEntity> notificationEntities = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        try {
            notificationService.insertAll(notificationEntities);
        } catch (Exception e) {
            log.error("error ocurred : " + e.getMessage());
            throw new CustomException("기프티콘 유효기간 알림 일괄 저장에 실패했습니다.");
        } finally {
            stopWatch.stop();
        }

        return "실행시간 " + stopWatch.getLastTaskTimeMillis() + "ms";
    }
}
