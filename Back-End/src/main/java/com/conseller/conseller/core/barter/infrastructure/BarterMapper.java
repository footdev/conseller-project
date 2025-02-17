package com.conseller.conseller.core.barter.infrastructure;

import com.conseller.conseller.core.barter.api.dto.request.BarterCreateRequest;
import com.conseller.conseller.core.barter.api.dto.response.BarterHostItemResponse;
import com.conseller.conseller.core.barter.api.dto.response.MyBarterResponse;
import com.conseller.conseller.core.barter.domain.BarterHostItem;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterEntity;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterHostItemEntity;
import com.conseller.conseller.core.category.infrastructure.SubCategoryEntity;
import com.conseller.conseller.core.gifticon.api.dto.response.GifticonResponse;
import com.conseller.conseller.core.user.infrastructure.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
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
    BarterEntity registBarterCreateToBarter(BarterCreateRequest barterCreateRequest, UserEntity userEntity, LocalDateTime endDate, SubCategoryEntity subCategoryEntity, SubCategoryEntity preferCategory);


    default BarterHostItemResponse toBarterItemData(BarterHostItem barterHostItem) {
        BarterHostItemEntity barterHostItemEntity = BarterHostItemEntity.of(barterHostItem);
        return BarterHostItemResponse.of(barterHostItemEntity);
    }

    //Barter -> MyBarterResponseDto 매핑
    default MyBarterResponse toMybarterResponseDto(BarterEntity barterEntity, List<BarterHostItemEntity> barterHostItemEntityHostItemEntities) {
        MyBarterResponse.MyBarterResponseBuilder barterResponse = MyBarterResponse.builder()
                .barterIdx(barterEntity.getBarterIdx())
                .barterName(barterEntity.getBarterName())
                .barterText(barterEntity.getBarterText())
                .barterStatus(barterEntity.getBarterStatus())
                .barterCreatedDate(convertString(barterEntity.getBarterCreatedDate()))
                .barterEndDate(convertString(barterEntity.getBarterEndDate()))
                .barterHostIdx(barterEntity.getBarterHost().getUserIdx())
                .subCategory(String.valueOf(barterEntity.getSubCategoryEntity().getSubCategoryIdx()));

        List<GifticonResponse> gifticonResponses = barterHostItemEntityHostItemEntities.stream()
                .map(item -> item.getGifticonEntity().toResponseDto())
                .collect(Collectors.toList());

        barterResponse.barterHostItems(gifticonResponses);

        if (barterEntity.getBarterCompleteGuest() != null) {
            barterResponse.barterCompleteGuestIdx(barterEntity.getBarterCompleteGuest().getUserIdx());
        }

        return barterResponse.build();
    }
}

