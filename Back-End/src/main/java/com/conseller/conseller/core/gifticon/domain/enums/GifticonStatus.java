package com.conseller.conseller.core.gifticon.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GifticonStatus {
    KEEP("보관"),
    AUCTION("경매"),
    STORE("판매"),
    BARTER("물물교환"),
    USED("사용");

    private final String status;
}
