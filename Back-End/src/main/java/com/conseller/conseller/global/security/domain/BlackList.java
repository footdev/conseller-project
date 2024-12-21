package com.conseller.conseller.global.security.domain;

import lombok.Builder;

@Builder
public class BlackList {
    private long id;
    private String accessToken;
    private long userIdx;
}
