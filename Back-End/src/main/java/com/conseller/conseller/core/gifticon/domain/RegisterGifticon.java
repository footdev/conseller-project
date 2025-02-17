package com.conseller.conseller.core.gifticon.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegisterGifticon {
    private String gifticonBarcode;
    private String gifticonName;
    private String gifticonEndDate;
    private int subCategory;
    private int mainCategory;
}
