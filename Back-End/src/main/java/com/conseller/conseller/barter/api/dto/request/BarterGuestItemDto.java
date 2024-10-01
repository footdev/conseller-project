package com.conseller.conseller.barter.api.dto.request;

import com.conseller.conseller.gifticon.api.dto.response.GifticonResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BarterGuestItemDto {
    private long barterRequestIdx;
    private GifticonResponse gifticon;

    @Builder
    public BarterGuestItemDto(long barterRequestIdx, GifticonResponse gifticon) {
        this.barterRequestIdx = barterRequestIdx;
        this.gifticon = gifticon;
    }
}
