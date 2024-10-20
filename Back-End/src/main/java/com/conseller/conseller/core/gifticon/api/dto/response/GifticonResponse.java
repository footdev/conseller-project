package com.conseller.conseller.core.gifticon.api.dto.response;

import lombok.*;

@Builder
@Getter @ToString
@RequiredArgsConstructor
public final class GifticonResponse {

    private long gifticonIdx;
    private String gifticonBarcode;
    private String gifticonName;
    private String gifticonStartDate;
    private String gifticonEndDate;
    private String gifticonAllImageUrl;
    private String gifticonDataImageUrl;
    private String gifticonStatus;
    private long userIdx;
    private long subCategoryIdx;
    private long mainCategoryIdx;
}
