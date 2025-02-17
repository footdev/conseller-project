package com.conseller.conseller.core.gifticon.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UsedGifticon {
    private long usedGifticonIdx;
    private String usedGifticonBarcode;
    private LocalDateTime usedGifticonDate;

    public UsedGifticon(String usedGifticonBarcode) {
        this.usedGifticonBarcode = usedGifticonBarcode;
        this.usedGifticonDate = LocalDateTime.now();
    }

    public UsedGifticon(long userGifticonIdx, String usedGifticonBarcode) {
        this.usedGifticonIdx = userGifticonIdx;
        this.usedGifticonBarcode = usedGifticonBarcode;
        this.usedGifticonDate = LocalDateTime.now();
    }
}
