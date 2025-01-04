package com.conseller.conseller.core.gifticon.domain;

import com.conseller.conseller.core.category.domain.MainCategory;
import com.conseller.conseller.core.category.domain.SubCategory;
import com.conseller.conseller.core.gifticon.api.dto.request.RegisterGifticon;
import com.conseller.conseller.core.gifticon.api.dto.response.GifticonResponse;
import com.conseller.conseller.core.gifticon.domain.enums.GifticonStatus;
import com.conseller.conseller.core.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter @Builder
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

    public static Gifticon of(RegisterGifticon registerGifticon, String allImageUrl, String cropImageUrl, long userId) {
        return Gifticon.builder()
                .gifticonAllImageUrl(allImageUrl)
                .gifticonBarcode(registerGifticon.getGifticonBarcode())
                .gifticonDataImageUrl(cropImageUrl)
                .gifticonEndDate(LocalDateTime.parse(registerGifticon.getGifticonEndDate()))
                .gifticonName(registerGifticon.getGifticonName())
                .gifticonStatus("ACTIVE")
                .user(User.builder().userIdx(userId).build())
                .subCategory(SubCategory.builder().subCategoryIdx(registerGifticon.getSubCategory()).build())
                .mainCategory(MainCategory.builder().mainCategoryIdx(registerGifticon.getMainCategory()).build())
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

    public void updateStatus(GifticonStatus gifticonStatus) {
        this.gifticonStatus = gifticonStatus.getStatus();
    }

    public long getSubCategoryIdx() {
        return subCategory.getSubCategoryIdx();
    }

    public void modifyUser(User user) {
        this.user = user;
    }

    public void transfer(User buyer) {
        this.user = buyer;
    }
}
