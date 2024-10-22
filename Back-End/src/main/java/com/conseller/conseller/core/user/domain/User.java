package com.conseller.conseller.core.user.domain;

import com.conseller.conseller.core.auction.domain.Auction;
import com.conseller.conseller.core.barter.domain.Barter;
import com.conseller.conseller.core.barter.domain.BarterRequest;
import com.conseller.conseller.core.bid.infrastructure.AuctionBidEntity;
import com.conseller.conseller.core.gifticon.domain.Gifticon;
import com.conseller.conseller.core.inquiry.domain.Inquiry;
import com.conseller.conseller.core.notification.domain.Notification;
import com.conseller.conseller.core.store.domain.Store;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class User {
    private long userIdx;
    private String userId;
    private String userPassword;
    private String userEmail;
    private String userPhoneNumber;
    private String userNickname;
    private char userGender;
    private int userAge;
    private long userDeposit;
    private LocalDateTime userDeletedDate;
    private String userName;
    private String userAccount;
    private String userAccountBank;
    private String userStatus;
    private LocalDateTime userRestrictEndDate;
    private int userRestrictCount;
    private String userProfileUrl;
    private String refreshToken;
    private String fcm;
    private String userPattern;
    private List<Auction> auctionEntities;
    private List<AuctionBidEntity> auctionBidEntities;
    private List<Barter> barterEntities;
    private List<BarterRequest> barterRequestEntities;
    private List<Gifticon> gifticonEntities;
    private List<Inquiry> inquiries;
    private List<Store> storeEntities;
    private List<Notification> notifications;
    private List<String> roles;
}
