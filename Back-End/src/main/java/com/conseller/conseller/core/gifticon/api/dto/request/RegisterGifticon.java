package com.conseller.conseller.core.gifticon.api.dto.request;

import lombok.*;

@Builder
@Getter
public class RegisterGifticon {

    private String gifticonBarcode;
    private String gifticonName;
    private String gifticonEndDate;
    private int subCategory;
    private int mainCategory;

    public RegisterGifticon toRegisterGifticon() {
        return RegisterGifticon.builder()
                .gifticonBarcode(gifticonBarcode)
                .gifticonName(gifticonName)
                .gifticonEndDate(gifticonEndDate)
                .subCategory(subCategory)
                .mainCategory(mainCategory)
                .build();
    }
}
