package com.conseller.conseller.core.barter.api.dto.mapper;

import com.conseller.conseller.core.barter.api.dto.request.BarterCreateDto;
import com.conseller.conseller.core.barter.api.dto.response.BarterItemData;
import com.conseller.conseller.core.barter.api.dto.response.MyBarterResponseDto;
import com.conseller.conseller.core.barter.infrastructure.BarterEntity;
import com.conseller.conseller.core.barter.infrastructure.BarterHostItem;
import com.conseller.conseller.core.category.infrastructure.SubCategoryEntity;
import com.conseller.conseller.core.gifticon.api.dto.response.GifticonResponse;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonEntity;
import com.conseller.conseller.core.user.infrastructure.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BarterMapper {

    BarterMapper INSTANCE = Mappers.getMapper(BarterMapper.class);

    //BarterCreateDto -> Barter 매핑
    @Mapping(source = "user", target = "barterHost")
    @Mapping(source = "endDate", target = "barterEndDate")
    @Mapping(source = "subCategory", target = "subCategory")
    @Mapping(source = "preferCategory", target = "preferSubCategory")
    BarterEntity registBarterCreateToBarter(BarterCreateDto barterCreateDto, UserEntity userEntity, LocalDateTime endDate, SubCategoryEntity subCategoryEntity, SubCategoryEntity preferCategory);


    default BarterItemData toBarterItemData(BarterEntity barterEntity) {
        List<BarterHostItem> barterHostItemList = barterEntity.getBarterHostItemList();
        List<GifticonEntity> gifticonEntityList = new ArrayList<>();
        GifticonEntity gifticonEntity = null;
        for(BarterHostItem gift : barterHostItemList) {
            if(gift.getGifticonEntity().getSubCategoryEntity() == barterEntity.getSubCategoryEntity()) {
                gifticonEntity = gift.getGifticonEntity();
                break;
            }
        }
        Boolean deposit = false;
        if(barterEntity.getBarterHost().getUserDeposit() > 0) {
            deposit = true;
        }

        String preferSubCategory = barterEntity.getPreferSubCategoryEntity().getSubCategoryContent();

        if(barterEntity.getPreferSubCategoryEntity().getSubCategoryIdx() > 10) {
            preferSubCategory = barterEntity.getPreferSubCategoryEntity().getMainCategoryEntity().getMainCategoryContent();
        }

        BarterItemData barterItemData = new BarterItemData();
        barterItemData.setBarterIdx(barterEntity.getBarterIdx());
        barterItemData.setGifticonDataImageName(gifticonEntity.getGifticonDataImageUrl());
        barterItemData.setGifticonName(gifticonEntity.getGifticonName());
        barterItemData.setGifticonEndDate(convertString(gifticonEntity.getGifticonEndDate()));
        barterItemData.setBarterEndDate(convertString(barterEntity.getBarterEndDate()));
        barterItemData.setDeposit(deposit);
        barterItemData.setPreper(preferSubCategory);
        barterItemData.setBarterName(barterEntity.getBarterName());

        return barterItemData;
    }

    //Barter -> MyBarterResponseDto 매핑
    default MyBarterResponseDto toMybarterResponseDto(BarterEntity barterEntity) {
        MyBarterResponseDto.MyBarterResponseDtoBuilder barterResponseDto = MyBarterResponseDto.builder()
                .barterIdx(barterEntity.getBarterIdx())
                .barterName(barterEntity.getBarterName())
                .barterText(barterEntity.getBarterText())
                .barterStatus(barterEntity.getBarterStatus())
                .barterCreatedDate(convertString(barterEntity.getBarterCreatedDate()))
                .barterEndDate(convertString(barterEntity.getBarterEndDate()))
                .barterHostIdx(barterEntity.getBarterHost().getUserIdx())
                .subCategory(String.valueOf(barterEntity.getSubCategoryEntity().getSubCategoryIdx()));

        List<GifticonResponse> gifticonResponses = barterEntity.getBarterHostItemList().stream()
                .map(item -> item.getGifticonEntity().toResponseDto())
                .collect(Collectors.toList());

        barterResponseDto.barterHostItems(gifticonResponses);

        if (barterEntity.getBarterCompleteGuest() != null) {
            barterResponseDto.barterCompleteGuestIdx(barterEntity.getBarterCompleteGuest().getUserIdx());
        }

        return barterResponseDto.build();
    }
}

