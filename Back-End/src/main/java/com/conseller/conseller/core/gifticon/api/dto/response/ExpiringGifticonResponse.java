package com.conseller.conseller.core.gifticon.api.dto.response;

import lombok.*;

@Getter
@RequiredArgsConstructor
public final class ExpiringGifticonResponse {
    private final long userIdx;
    private final int expiryDay;
    private final String gifticonName;
    private final int gifticonCnt;
}
