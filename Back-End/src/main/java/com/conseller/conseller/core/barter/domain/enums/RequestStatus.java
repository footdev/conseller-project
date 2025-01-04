package com.conseller.conseller.core.barter.domain.enums;

import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum RequestStatus {
    WAIT("요청"),
    ACCEPTED("수락"),
    REJECTED("거절");

    private final String status;

    public static RequestStatus find(String inputStatus) {
        return Arrays.stream(RequestStatus.values())
                .filter(status -> status.getStatus().equals(inputStatus))
                .findFirst()
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.INVALID_BARTER_STATUS));
    }
}
