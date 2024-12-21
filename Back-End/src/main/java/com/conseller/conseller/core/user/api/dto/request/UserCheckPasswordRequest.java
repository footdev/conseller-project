package com.conseller.conseller.core.user.api.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserCheckPasswordRequest {
    private long userIdx;
    private String userPassword;
}
