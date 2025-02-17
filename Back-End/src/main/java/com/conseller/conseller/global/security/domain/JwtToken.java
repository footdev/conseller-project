package com.conseller.conseller.global.security.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter @Builder @ToString
public class JwtToken {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
