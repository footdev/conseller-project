package com.conseller.conseller.core.user.api.dto.response;

import lombok.*;

@Builder
public class LoginResponse {
    private Long userIdx;
    private String userNickname;
    private String accessToken;
    private String refreshToken;
}
