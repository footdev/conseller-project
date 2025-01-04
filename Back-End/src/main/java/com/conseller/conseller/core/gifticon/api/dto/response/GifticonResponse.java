package com.conseller.conseller.core.gifticon.api.dto.response;

import com.conseller.conseller.core.barter.domain.Gifticons;
import com.conseller.conseller.core.gifticon.domain.Gifticon;
import com.conseller.conseller.global.utils.DateTimeConverter;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.conseller.conseller.global.utils.DateTimeConverter.convertString;

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

    public static GifticonResponse of(Gifticon gifticon) {
        return GifticonResponse.builder()
                .gifticonIdx(gifticon.getGifticonIdx())
                .gifticonBarcode(gifticon.getGifticonBarcode())
                .gifticonName(gifticon.getGifticonName())
                .gifticonStartDate(convertString(gifticon.getGifticonStartDate()))
                .gifticonEndDate(convertString(gifticon.getGifticonEndDate()))
                .gifticonAllImageUrl(gifticon.getGifticonAllImageUrl())
                .gifticonDataImageUrl(gifticon.getGifticonDataImageUrl())
                .gifticonStatus(gifticon.getGifticonStatus())
                .userIdx(gifticon.getUser().getUserIdx())
                .subCategoryIdx(gifticon.getSubCategoryIdx())
                .mainCategoryIdx(gifticon.getMainCategory().getMainCategoryIdx())
                .build();
    }

    public static List<GifticonResponse> ofAll(Gifticons gifticons) {
        return gifticons.getGifticons().stream()
                .map(GifticonResponse::of)
                .collect(Collectors.toList());
    }
}
