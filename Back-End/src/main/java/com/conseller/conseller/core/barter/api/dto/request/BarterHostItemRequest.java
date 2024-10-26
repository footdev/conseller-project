package com.conseller.conseller.core.barter.api.dto.request;

import com.conseller.conseller.core.gifticon.api.dto.response.GifticonResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BarterHostItemRequest {
    private long barterIdx;
    private GifticonResponse gifticon;

    @Builder
    public BarterHostItemRequest(long barterIdx, GifticonResponse gifticon){
        this.barterIdx = barterIdx;
        this.gifticon = gifticon;
    }

}
