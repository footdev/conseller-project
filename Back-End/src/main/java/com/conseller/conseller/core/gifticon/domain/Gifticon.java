package com.conseller.conseller.core.gifticon.domain;

import com.conseller.conseller.core.category.domain.MainCategory;
import com.conseller.conseller.core.category.domain.SubCategory;
import com.conseller.conseller.core.gifticon.api.dto.request.GifticonRegisterRequest;
import com.conseller.conseller.core.gifticon.api.dto.response.GifticonResponse;
import com.conseller.conseller.core.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter @Builder
@RequiredArgsConstructor
public class Gifticon {
    private long gifticonIdx;
    private String gifticonBarcode;
    private String gifticonName;
    private LocalDateTime gifticonStartDate;
    private LocalDateTime gifticonEndDate;
    private String gifticonAllImageUrl;
    private String gifticonDataImageUrl;
    private String gifticonStatus;
    private User user;
    private SubCategory subCategory;
    private MainCategory mainCategory;

    public static Gifticon of(GifticonRegisterRequest gifticonRegisterRequest, String allImageUrl, String cropImageUrl, long userId) {
        return Gifticon.builder()
                .gifticonAllImageUrl(allImageUrl)
                .gifticonBarcode(gifticonRegisterRequest.getGifticonBarcode())
                .gifticonDataImageUrl(cropImageUrl)
                .gifticonEndDate(LocalDateTime.parse(gifticonRegisterRequest.getGifticonEndDate()))
                .gifticonName(gifticonRegisterRequest.getGifticonName())
                .gifticonStatus("ACTIVE")
                .user(User.builder().userIdx(userId).build())
                .subCategory(SubCategory.builder().subCategoryIdx(gifticonRegisterRequest.getSubCategory()).build())
                .mainCategory(MainCategory.builder().mainCategoryIdx(gifticonRegisterRequest.getMainCategory()).build())
                .build();
    }

    public GifticonResponse toResponse() {
        return GifticonResponse.builder()
                .gifticonIdx(gifticonIdx)
                .gifticonBarcode(gifticonBarcode)
                .gifticonName(gifticonName)
                .gifticonStartDate(gifticonStartDate.toString())
                .gifticonEndDate(gifticonEndDate.toString())
                .gifticonAllImageUrl(gifticonAllImageUrl)
                .gifticonDataImageUrl(gifticonDataImageUrl)
                .gifticonStatus(gifticonStatus)
                .userIdx(user.getUserIdx())
                .subCategoryIdx(subCategory.getSubCategoryIdx())
                .mainCategoryIdx(mainCategory.getMainCategoryIdx())
                .build();
    }
}
