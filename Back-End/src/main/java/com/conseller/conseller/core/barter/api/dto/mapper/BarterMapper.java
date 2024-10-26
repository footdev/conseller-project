package com.conseller.conseller.core.barter.api.dto.mapper;

import com.conseller.conseller.core.barter.api.dto.request.BarterCreateRequest;
import com.conseller.conseller.core.barter.api.dto.response.BarterHostItemResponse;
import com.conseller.conseller.core.barter.api.dto.response.MyBarterResponse;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterEntity;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterHostItemEntity;
import com.conseller.conseller.core.category.infrastructure.SubCategory;
import com.conseller.conseller.core.gifticon.api.dto.response.GifticonResponse;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonEntity;
import com.conseller.conseller.core.user.infrastructure.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.conseller.conseller.global.utils.DateTimeConverter.convertString;

@Mapper(componentModel = "spring")
public interface BarterMapper {

    BarterMapper INSTANCE = Mappers.getMapper(BarterMapper.class);

    //BarterCreateDto -> Barter 매핑
    @Mapping(source = "user", target = "barterHost")
    @Mapping(source = "endDate", target = "barterEndDate")
    @Mapping(source = "subCategory", target = "subCategory")
    @Mapping(source = "preferCategory", target = "preferSubCategory")
    BarterEntity registBarterCreateToBarter(BarterCreateRequest barterCreateRequest, User user, LocalDateTime endDate, SubCategory subCategory, SubCategory preferCategory);


    default BarterHostItemResponse toBarterItemData(BarterEntity barterEntity, List<BarterHostItemEntity> barterHostItemEntities) {
        List<BarterHostItemEntity> barterHostItemEntityList = barterHostItemEntities;
        List<GifticonEntity> gifticonEntityList = new ArrayList<>();
        GifticonEntity gifticonEntity = null;
        for(BarterHostItemEntity gift : barterHostItemEntityList) {
            if(gift.getGifticonEntity().getSubCategory() == barterEntity.getSubCategory()) {
                gifticonEntity = gift.getGifticonEntity();
                break;
            }
        }
        Boolean deposit = false;
        if(barterEntity.getBarterHost().getUserDeposit() > 0) {
            deposit = true;
        }

        String preferSubCategory = barterEntity.getPreferSubCategory().getSubCategoryContent();

        if(barterEntity.getPreferSubCategory().getSubCategoryIdx() > 10) {
            preferSubCategory = barterEntity.getPreferSubCategory().getMainCategoryEntity().getMainCategoryContent();
        }

        BarterHostItemResponse barterHostItemResponse = new BarterHostItemResponse();
        barterHostItemResponse.setBarterIdx(barterEntity.getBarterIdx());
        barterHostItemResponse.setGifticonDataImageName(gifticonEntity.getGifticonDataImageUrl());
        barterHostItemResponse.setGifticonName(gifticonEntity.getGifticonName());
        barterHostItemResponse.setGifticonEndDate(convertString(gifticonEntity.getGifticonEndDate()));
        barterHostItemResponse.setBarterEndDate(convertString(barterEntity.getBarterEndDate()));
        barterHostItemResponse.setDeposit(deposit);
        barterHostItemResponse.setPreper(preferSubCategory);
        barterHostItemResponse.setBarterName(barterEntity.getBarterName());

        return barterHostItemResponse;
    }

    //Barter -> MyBarterResponseDto 매핑
    default MyBarterResponse toMybarterResponseDto(BarterEntity barterEntity, List<BarterHostItemEntity> barterHostItemEntities) {
        MyBarterResponse.MyBarterResponseBuilder barterResponse = MyBarterResponse.builder()
                .barterIdx(barterEntity.getBarterIdx())
                .barterName(barterEntity.getBarterName())
                .barterText(barterEntity.getBarterText())
                .barterStatus(barterEntity.getBarterStatus())
                .barterCreatedDate(convertString(barterEntity.getBarterCreatedDate()))
                .barterEndDate(convertString(barterEntity.getBarterEndDate()))
                .barterHostIdx(barterEntity.getBarterHost().getUserIdx())
                .subCategory(String.valueOf(barterEntity.getSubCategory().getSubCategoryIdx()));

        List<GifticonResponse> gifticonResponses = barterHostItemEntities.stream()
                .map(item -> item.getGifticonEntity().toResponseDto())
                .collect(Collectors.toList());

        barterResponse.barterHostItems(gifticonResponses);

        if (barterEntity.getBarterCompleteGuest() != null) {
            barterResponse.barterCompleteGuestIdx(barterEntity.getBarterCompleteGuest().getUserIdx());
        }

        return barterResponse.build();
    }
}

