package com.conseller.conseller.notification;

import com.conseller.conseller.entity.NotificationEntity;
import com.conseller.conseller.notification.dto.request.NotificationAnswerRequest;
import com.conseller.conseller.notification.dto.response.NotificationListResponse;

import java.util.List;

public interface NotificationService {
    public void sendAuctionNotification(Long auctionIdx, String title, String body, Integer index, Integer type);

    public void sendAuctionBidNotification(Long auctionIdx, Long userIdx , String title, Integer type);

    public void sendStoreNotification(Long storeIdx, String title, String body, Integer index, Integer type);

    public void sendBarterNotification(Long barterIdx, String title, Integer type);

    public void sendBarterExpiredNotification(Long barterIdx, String title, Integer type);

    public void sendBarterRequestRejectedNotification(Long barterReuestIdx, String title, Integer type);

    public void sendBarterRequestNotification(Long barterIdx, String title, Integer type);

    public NotificationEntity createGifticonNotification(Long userIdx, Integer remainDay, String gifticionName, Long gifticonCount, Integer type);

    public void sendNotification(Long userIdx, String title, String body);

    public NotificationListResponse getNotificationList(Long userIdx);

    public void getAnswer(Long userIdx, NotificationAnswerRequest request);

    public void insertAll(List<NotificationEntity> notificationEntities);

    void saveAll(List<NotificationEntity> notificationEntities);
}
