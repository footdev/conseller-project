package com.conseller.conseller.core.user.domain;

import com.conseller.conseller.core.auction.domain.Auction;
import com.conseller.conseller.core.barter.domain.Barter;
import com.conseller.conseller.core.barter.domain.BarterRequest;
import com.conseller.conseller.core.bid.infrastructure.BiddingEntity;
import com.conseller.conseller.core.gifticon.domain.Gifticon;
import com.conseller.conseller.core.inquiry.domain.Inquiry;
import com.conseller.conseller.core.notification.domain.Notification;
import com.conseller.conseller.core.store.domain.Store;
import com.conseller.conseller.core.user.api.dto.request.UserInfoRequest;
import com.conseller.conseller.core.user.domain.enums.UserStatus;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    private long userCash;
    private LocalDateTime userDeletedDate;
    private String userName;
    private UserStatus userStatus;
    private LocalDateTime userRestrictEndDate;
    private int userRestrictCount;
    private String userProfileUrl;
    private String refreshToken;
    private String fcm;
    private String userPattern;
    private List<String> roles;

    public void chargeCash(long cash) {
        this.userCash += cash;
    }

    public void decreaseCash(int price) {
        this.userCash -= price;
    }

    public void increaseCash(int price) {
        this.userCash += price;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateUserInfo(UserInfoRequest userInfoRequest) {
        this.userPassword = userInfoRequest.getUserPassword();
        this.userNickname = userInfoRequest.getUserNickname();
        this.userEmail = userInfoRequest.getUserEmail();

        if (userInfoRequest.getUserPassword() != null) {
            encryptPassword(new BCryptPasswordEncoder());
        }
    }

    public void encryptPassword(PasswordEncoder passwordEncoder) {
        this.userPassword = passwordEncoder.encode(this.userPassword);
    }

    public void updateTempPassword(String tempPassword) {
        this.userPassword = tempPassword;
    }

    public void updateProfile(String profileUrl) {
        this.userProfileUrl = profileUrl;
    }

    public void delete() {
        this.userStatus = UserStatus.DELETED;
        this.refreshToken = null;
        this.userDeletedDate = LocalDateTime.now();
    }

    public void updateFirebaseToken(String firebaseToken) {
        this.fcm = firebaseToken;
    }

    public void updatePattern(String pattern) {
        this.userPattern = pattern;
    }
}
