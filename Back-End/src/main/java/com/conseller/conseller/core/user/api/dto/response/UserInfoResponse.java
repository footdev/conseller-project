package com.conseller.conseller.core.user.api.dto.response;

import com.conseller.conseller.core.user.domain.User;
import lombok.*;

@Builder
@Getter
public class UserInfoResponse {

    private String userId;
    private String userPassword;
    private String userNickname;
    private String userEmail;
    private String userProfileUrl;
    private String userAccount;
    private String userAccountBank;
    private String userPhoneNumber;

    public static UserInfoResponse of(User user) {
        return UserInfoResponse.builder()
                .userId(user.getUserId())
                .userPassword(user.getUserPassword())
                .userNickname(user.getUserNickname())
                .userEmail(user.getUserEmail())
                .userProfileUrl(user.getUserProfileUrl())
                .userPhoneNumber(user.getUserPhoneNumber())
                .build();
    }
}
