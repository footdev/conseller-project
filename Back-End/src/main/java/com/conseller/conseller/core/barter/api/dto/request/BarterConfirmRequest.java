package com.conseller.conseller.core.barter.api.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class BarterConfirmRequest {
    private final Long barterIdx;
    private final Long userIdx;
    private final Long barterRequestIdx;
}
