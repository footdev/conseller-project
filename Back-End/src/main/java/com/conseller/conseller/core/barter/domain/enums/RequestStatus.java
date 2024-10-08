package com.conseller.conseller.core.barter.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RequestStatus {
    WAIT("요청"),
    ACCEPTED("수락"),
    REJECTED("거절");

    private final String status;
}
