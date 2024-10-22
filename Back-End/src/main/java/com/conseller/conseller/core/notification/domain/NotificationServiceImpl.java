package com.conseller.conseller.core.notification.domain;

import com.conseller.conseller.core.auction.infrastructure.AuctionEntity;
import com.conseller.conseller.core.auction.infrastructure.AuctionRepository;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterEntity;
import com.conseller.conseller.core.barter.infrastructure.BarterRepository;
import com.conseller.conseller.core.barter.domain.enums.BarterStatus;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterRequestEntity;
import com.conseller.conseller.core.barter.infrastructure.BarterRequestRepository;
import com.conseller.conseller.core.barter.domain.enums.RequestStatus;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import com.conseller.conseller.core.notification.api.dto.mapper.NotificationMapper;
import com.conseller.conseller.core.notification.api.dto.request.NotificationAnswerRequest;
import com.conseller.conseller.core.notification.api.dto.response.NotificationItemData;
import com.conseller.conseller.core.notification.api.dto.response.NotificationListResponse;
import com.conseller.conseller.core.notification.infrastructure.NotificationEntity;
import com.conseller.conseller.core.notification.infrastructure.NotificationJdbcRepository;
import com.conseller.conseller.core.notification.infrastructure.NotificationRepository;
import com.conseller.conseller.core.store.infrastructure.StoreEntity;
import com.conseller.conseller.core.store.infrastructure.StoreRepository;
import com.conseller.conseller.core.user.infrastructure.UserEntity;
import com.conseller.conseller.core.user.infrastructure.UserRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.conseller.conseller.global.utils.DateTimeConverter.convertString;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationServiceImpl implements NotificationService{
    private final AuctionRepository auctionRepository;
    private final StoreRepository storeRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final BarterRequestRepository barterRequestRepository;
    private final BarterRepository barterRepository;
    private final NotificationJdbcRepository notificationJdbcRepository;

    @Override
    public void sendAuctionNotification(Long auctionIdx, String title, String body, Integer index, Integer type) {
//        Auction auction = auctionRepository.findById(auctionIdx)
//                .orElseThrow(() -> new CustomException(CustomExceptionStatus.AUCTION_INVALID));
//
//        User user = null;
//        String contents = null;
//
//        if(index == 1) { // 구매자
//            if(auction.getHighestBidUser().getFcm() == null)
//                return;
//
//            user = auction.getHighestBidUser();
//
//            contents = auction.getUser().getUserNickname() + " " + body;
//        } else if( index == 2) { // 판매자
//            if(auction.getUser().getFcm() == null)
//                return;
//
//            user = auction.getUser();
//
//            contents = auction.getHighestBidUser().getUserNickname() + " " + body;
//        } else {
//            throw new CustomException(CustomExceptionStatus.INVALID_NOTI_TYPE);
//        }
//
//
//        Notification notification = Notification.builder()
//                .setTitle(title)
//                .setBody(contents)
//                .build();
//
//        Message message = Message.builder()
//                .setNotification(notification)
//                .setToken(user.getFcm())
//                .putData("timestamp", convertString(LocalDateTime.now()))
//                .build();
//
//        try{
//            String response = FirebaseMessaging.getInstance().send(message);
//
//            log.info(response);
//
//            NotificationEntity notificationEntity;
//
//            if(index == 1) {
//                notificationEntity = NotificationEntity.from(title, contents, type, false, auction.getHighestBidUser());
//            }else {
//                notificationEntity = NotificationEntity.from(title, contents, type, true, auction.getUser());
//            }
//
//            notificationRepository.save(notificationEntity);
//        }catch (Exception e){
//            log.warn(auction.getUser().getUserId() + ": 알림 전송에 실패하였습니다.");
//        }
    }

    @Override
    public void sendAuctionBidNotification(Long auctionIdx, Long userIdx, String title, Integer type) {
        AuctionEntity auctionEntity = auctionRepository.findById(auctionIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.AUCTION_INVALID));

        UserEntity bidder = userRepository.findById(userIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));

        UserEntity userEntity = auctionEntity.getUserEntity();

        if(userEntity.getFcm() == null)
            return;

        String contents = bidder.getUserNickname() + " 님이 입찰하셨습니다.";



        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(contents)
                .build();

        Message message = Message.builder()
                .setNotification(notification)
                .setToken(userEntity.getFcm())
                .putData("timestamp", convertString(LocalDateTime.now()))
                .build();

        try{
            String response = FirebaseMessaging.getInstance().send(message);

            log.info(response);

            //데이터베이스 저장
            NotificationEntity notificationEntity = NotificationEntity.from(title, contents, type, false, auctionEntity.getUserEntity());

            notificationRepository.save(notificationEntity);

        }catch (Exception e){
            log.warn(auctionEntity.getUserEntity().getUserId() + ": 알림 전송에 실패하였습니다.");
        }
    }

    @Override
    public void sendStoreNotification(Long storeIdx, String title, String body, Integer index, Integer type) {
        StoreEntity storeEntity = storeRepository.findById(storeIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.STORE_INVALID));

        UserEntity userEntity = null;
        String contents = null;

        if(index == 1) { // 구매자
            if(storeEntity.getConsumerEntity().getFcm() == null)
                return;

            userEntity = storeEntity.getConsumerEntity();

            contents = storeEntity.getUserEntity().getUserNickname() + " " + body;
        } else if( index == 2) { // 판매자
            if(storeEntity.getUserEntity().getFcm() == null)
                return;

            userEntity = storeEntity.getUserEntity();

            contents = storeEntity.getConsumerEntity().getUserNickname() + " " + body;
        } else {
            throw new CustomException(CustomExceptionStatus.INVALID_NOTI_TYPE);
        }


        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(contents)
                .build();

        Message message = Message.builder()
                .setNotification(notification)
                .setToken(userEntity.getFcm())
                .putData("timestamp", convertString(LocalDateTime.now()))
                .build();

        try{
            String response = FirebaseMessaging.getInstance().send(message);

            NotificationEntity notificationEntity;

            if(index == 1) {
                notificationEntity = NotificationEntity.from(title, contents, type, false, storeEntity.getConsumerEntity());
            }else {
                notificationEntity = NotificationEntity.from(title, contents, type, true, storeEntity.getConsumerEntity());
            }

            notificationRepository.save(notificationEntity);
        }catch (Exception e){
            log.warn(storeEntity.getUserEntity().getUserId() + ": 알림 전송에 실패하였습니다.");
        }
    }

    @Override
    public void sendBarterNotification(Long barterIdx, String title, Integer type) {
        List<BarterRequestEntity> requestList = barterRequestRepository.findByBarterIdx(barterIdx);

        for(BarterRequestEntity barter : requestList) {
            if(barter.getUserEntity().getFcm() == null)
                return;

            String contents = null;

            if(barter.getBarterRequestStatus().equals(RequestStatus.ACCEPTED.getStatus())){
                contents = barter.getUserEntity().getUserNickname() + " 님의 요청이 수락되었습니다.";
            }else {
                contents = barter.getUserEntity().getUserNickname() + " 님의 요청이 거절되었습니다.";
            }

            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(contents)
                    .build();

            Message message = Message.builder()
                    .setNotification(notification)
                    .setToken(barter.getUserEntity().getFcm())
                    .putData("timestamp", convertString(LocalDateTime.now()))
                    .build();

            try{
                String response = FirebaseMessaging.getInstance().send(message);

                //데이터베이스 저장
                NotificationEntity notificationEntity = NotificationEntity.from(title, contents, type, false, barter.getUserEntity());

                notificationRepository.save(notificationEntity);


            }catch (Exception e){
                log.warn(barter.getUserEntity().getUserId() + ": 알림 전송에 실패하였습니다.");
            }
        }
    }

    @Override
    public void sendBarterExpiredNotification(Long barterIdx, String title, Integer type) {
        BarterEntity barterEntity = barterRepository.findByBarterIdx(barterIdx)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 교환글입니다."));

        if(barterEntity.getBarterHost().getFcm() == null)
            return;

        String contents = null;
        if(barterEntity.getBarterStatus().equals(BarterStatus.EXPIRED.getStatus())) {
            contents = barterEntity.getBarterName() + " 교환글이 만료되었습니다.";
        }

        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(contents)
                .build();

        Message message = Message.builder()
                .setNotification(notification)
                .setToken(barterEntity.getBarterHost().getFcm())
                .putData("timestamp", convertString(LocalDateTime.now()))
                .build();

        try{
            String response = FirebaseMessaging.getInstance().send(message);
            NotificationEntity notificationEntity = NotificationEntity.from(title, contents, type, false, barterEntity.getBarterHost());
            notificationRepository.save(notificationEntity);
        }catch (Exception e){
            log.warn(barterEntity.getBarterHost().getUserId() + ": 알림 전송에 실패하였습니다.");
        }
    }

    @Override
    public void sendBarterRequestRejectedNotification(Long barterRequestIdx, String title, Integer type) {
        BarterRequestEntity barterRequestEntity = barterRequestRepository.findByBarterRequestIdx(barterRequestIdx)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 교환신청 입니다."));

        if(barterRequestEntity.getUserEntity().getFcm() == null) return;

        String contents = null;
        if(barterRequestEntity.getBarterRequestStatus().equals(RequestStatus.REJECTED)) {
            contents = barterRequestEntity.getBarterEntity().getBarterName() + " 에 대한 교환 신청이 거절되었습니다.";
        }

        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(contents)
                .build();

        Message message = Message.builder()
                .setNotification(notification)
                .setToken(barterRequestEntity.getUserEntity().getFcm())
                .putData("timestamp", convertString(LocalDateTime.now()))
                .build();

        try{
//            String response = FirebaseMessaging.getInstance().send(message);

            NotificationEntity expirationDateNotification = NotificationEntity.from(title, contents, type, false, barterRequestEntity.getUserEntity());

            notificationRepository.save(expirationDateNotification);

        }catch (Exception e){
            log.warn(barterRequestEntity.getUserEntity().getUserId() + ": 알림 전송에 실패하였습니다.");
        }
    }

    @Override
    public void sendBarterRequestNotification(Long barterIdx, String title, Integer type){
        BarterEntity barterEntity = barterRepository.findByBarterIdx(barterIdx)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 교환글입니다."));

        if(barterEntity.getBarterHost().getFcm() == null) return;

        String contents = null;
        if(barterEntity.getBarterStatus().equals(BarterStatus.SUGGESTED.getStatus())) {
            contents = barterEntity.getBarterName() + " 에 대한 새로운 교환신청!";
        }

        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(contents)
                .build();

        Message message = Message.builder()
                .setNotification(notification)
                .setToken(barterEntity.getBarterHost().getFcm())
                .putData("timestamp", convertString(LocalDateTime.now()))
                .build();

        try{
            String response = FirebaseMessaging.getInstance().send(message);

            //데이터베이스 저장
            NotificationEntity notificationEntity = NotificationEntity.from(title, contents, type, false, barterEntity.getBarterHost());

            notificationRepository.save(notificationEntity);


        }catch (Exception e){
            log.warn(barterEntity.getBarterHost().getUserId() + ": 알림 전송에 실패하였습니다.");
        }
    }

    @Override
    public NotificationEntity createGifticonNotification(Long userIdx, Integer remainDay, String gifticionName, Long gifticonCount, Integer type) {
        UserEntity userEntity = userRepository.findById(userIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.GIFTICON_INVALID));

//        if(user.getFcm() == null) {
//            log.info(user.getUserId() + ": fcm token is empty");
//            return;
//        }

        String title = "기프티콘 알림";
        String body = " ";

        if(gifticonCount == 1){
            body = gifticionName + " 기프티콘 유효기간이 " + remainDay + "일 남았습니다.";
        }else {
            body = gifticionName + " 외 " + (gifticonCount - 1) + "개의 기프티콘 유효기간이 " + remainDay + "일 남았습니다.";
        }

        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

//        Message message = Message.builder()
//                .setNotification(notification)
//                .setToken(user.getFcm())
//                .putData("timestamp", convertString(LocalDateTime.now()))
//                .build();

//        try {
//            String response = FirebaseMessaging.getInstance().send(message);
//        } catch (Exception e){
//            log.warn(user.getUserId() + ": 알림 전송에 실패하였습니다.");
//        }

        return NotificationEntity.from(title, body, LocalDateTime.now(), type, false, userEntity);
    }

    public void insertAll(List<NotificationEntity> notificationEntities)  {
        notificationJdbcRepository.batchInsertExpirationDateNotifications(notificationEntities);
    }

    @Override
    public void saveAll(List<NotificationEntity> notificationEntities) {
        notificationRepository.saveAll(notificationEntities);
    }


    @Override
    public void sendNotification(Long userIdx, String title, String body) {
        UserEntity userEntity = userRepository.findById(userIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));

        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setNotification(notification)
                .setToken(userEntity.getFcm())
                .putData("timestamp", convertString(LocalDateTime.now()))
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            NotificationEntity notificationEntity = NotificationEntity.from(title, body, 1, true, userEntity);
            notificationRepository.save(notificationEntity);
        } catch (Exception e){
            log.warn(userEntity.getUserId() + ": 알림 전송에 실패하였습니다.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationListResponse getNotificationList(Long userIdx) {
        List<NotificationEntity> notificationEntityList = notificationRepository.findByNotificationUser_UserIdx(userIdx);

        List<NotificationItemData> notificationItemDataList = NotificationMapper.INSTANCE.notificationsToItemDatas(notificationEntityList);

        NotificationListResponse response = new NotificationListResponse(notificationItemDataList);

        return response;
    }

    @Override
    public void getAnswer(Long userIdx, NotificationAnswerRequest request) {
        NotificationEntity notification = notificationRepository.findById(request.getNotificationIdx())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 알림입니다."));

        //알림 로직
        if(request.getNotificationAnswer()){
            notification.updateType(6);
        }
        else {
            notificationRepository.deleteById(request.getNotificationIdx());
        }
    }


}
