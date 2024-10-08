package com.conseller.conseller.core.user.domain;

import com.conseller.conseller.core.auction.infrastructure.AuctionEntity;
import com.conseller.conseller.core.barter.infrastructure.BarterEntity;
import com.conseller.conseller.core.barter.infrastructure.BarterRequestEntity;
import com.conseller.conseller.core.bid.infrastructure.AuctionBid;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonEntity;
import com.conseller.conseller.core.inquiry.infrastructure.InquiryEntity;
import com.conseller.conseller.core.notification.infrastructure.NotificationEntity;
import com.conseller.conseller.core.store.infrastructure.StoreEntity;
import com.conseller.conseller.core.user.infrastructure.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class User {
    private Long userIdx;
    private String userId;
    private String userPassword;
    private String userEmail;
    private String userPhoneNumber;
    private String userNickname;
    private Character userGender;
    private Integer userAge;
    private Long userDeposit;
    private LocalDateTime userDeletedDate;
    private String userName;
    private String userAccount;
    private String userAccountBank;
    private String userStatus;
    private LocalDateTime userRestrictEndDate;
    private Integer userRestrictCount;
    private String userProfileUrl;
    private String refreshToken;
    private String fcm;
    private String userPattern;
    private List<AuctionEntity> auctionEntities;
    private List<AuctionBid> auctionBids;
    private List<BarterEntity> barterEntities;
    private List<BarterRequestEntity> barterRequestEntities;
    private List<GifticonEntity> gifticonEntities;
    private List<InquiryEntity> inquiries;
    private List<StoreEntity> storeEntities;
    private List<NotificationEntity> notificationEntities;
    private List<String> roles;

    public static User of (UserEntity userEntity) {
        return User.builder()
                .userIdx(userEntity.getUserIdx())
                .userId(userEntity.getUserId())
                .userPassword(userEntity.getUserPassword())
                .userEmail(userEntity.getUserEmail())
                .userPhoneNumber(userEntity.getUserPhoneNumber())
                .userNickname(userEntity.getUserNickname())
                .userGender(userEntity.getUserGender())
                .userAge(userEntity.getUserAge())
                .userDeposit(userEntity.getUserDeposit())
                .userDeletedDate(userEntity.getUserDeletedDate())
                .userName(userEntity.getName())
                .userAccount(userEntity.getUserAccount())
                .userAccountBank(userEntity.getUserAccountBank())
                .userStatus(userEntity.getUserStatus())
                .userRestrictEndDate(userEntity.getUserRestrictEndDate())
                .userRestrictCount(userEntity.getUserRestrictCount())
                .userProfileUrl(userEntity.getUserProfileUrl())
                .refreshToken(userEntity.getRefreshToken())
                .fcm(userEntity.getFcm())
                .userPattern(userEntity.getUserPattern())
                .auctionEntities(userEntity.getAuctionEntities())
                .auctionBids(userEntity.getAuctionBids())
                .barterEntities(userEntity.getBarterEntities())
                .barterRequestEntities(userEntity.getBarterRequestEntities())
                .gifticonEntities(userEntity.getGifticonEntities())
                .inquiries(userEntity.getInquiries())
                .storeEntities(userEntity.getStoreEntities())
                .notificationEntities(userEntity.getNotificationEntities())
                .roles(userEntity.getRoles())
                .build();
    }
}
