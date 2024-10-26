package com.conseller.conseller.core.barter.api.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class BarterConfirmRequest {
    private Long barterIdx;
    private Long userIdx;
    private Boolean confirm;
}
