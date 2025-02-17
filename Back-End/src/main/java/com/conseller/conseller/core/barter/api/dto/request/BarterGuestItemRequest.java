package com.conseller.conseller.core.barter.api.dto.request;

import com.conseller.conseller.core.gifticon.api.dto.response.GifticonResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BarterGuestItemRequest {
    private long barterRequestIdx;
    private GifticonResponse gifticon;

    @Builder
    public BarterGuestItemRequest(long barterRequestIdx, GifticonResponse gifticon) {
        this.barterRequestIdx = barterRequestIdx;
        this.gifticon = gifticon;
    }
}
