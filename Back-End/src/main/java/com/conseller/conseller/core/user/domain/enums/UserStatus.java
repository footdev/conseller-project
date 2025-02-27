package com.conseller.conseller.core.user.domain.enums;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatus {
    RESTRICTED("제한"),
    DELETED("탈퇴"),
    ACTIVE("정상");

    private final String status;
}
