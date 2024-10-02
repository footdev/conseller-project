package com.conseller.conseller.user.domain;

import com.conseller.conseller.auction.infrastructure.AuctionEntity;
import com.conseller.conseller.barter.infrastructure.Barter;
import com.conseller.conseller.barter.infrastructure.BarterRequest;
import com.conseller.conseller.entity.*;
import com.conseller.conseller.user.infrastructure.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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
    private List<Barter> barters;
    private List<BarterRequest> barterRequests;
    private List<Gifticon> gifticons;
    private List<Inquiry> inquiries;
    private List<Store> stores;
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
                .barters(userEntity.getBarters())
                .barterRequests(userEntity.getBarterRequests())
                .gifticons(userEntity.getGifticons())
                .inquiries(userEntity.getInquiries())
                .stores(userEntity.getStores())
                .notificationEntities(userEntity.getNotificationEntities())
                .roles(userEntity.getRoles())
                .build();
    }
}
