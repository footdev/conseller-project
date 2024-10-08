package com.conseller.conseller.core.inquiry.api.dto.mapper;

import com.conseller.conseller.core.inquiry.infrastructure.InquiryEntity;
import com.conseller.conseller.core.user.infrastructure.UserEntity;
import com.conseller.conseller.core.inquiry.api.dto.request.RegistInquiryRequest;
import com.conseller.conseller.core.inquiry.api.dto.response.DetailInquiryResponse;
import com.conseller.conseller.core.inquiry.api.dto.response.InquiryItemData;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InquiryMapper {
    InquiryMapper INSTANCE = Mappers.getMapper(InquiryMapper.class);

    //RegistInquiryRequest -> Inquiry 매핑
    @Mapping(source = "user", target = "user")
    @Mapping(source = "reportedUser", target = "reportedUser")
    InquiryEntity registInquiryToInquiry(RegistInquiryRequest request, UserEntity userEntity, UserEntity reportedUserEntity);

    //User, Inquiry -> DetailInquiryResponse
    @Mapping(source = "user.userIdx", target = "userIdx")
    DetailInquiryResponse entityToDetailInquiryResponse(UserEntity userEntity, InquiryEntity inquiryEntity);

    //InquiryList -> InquiryItemDataList 매핑
    @Named("I2I")
    default InquiryItemData inquiryToItemData(InquiryEntity inquiryEntity){
        InquiryItemData itemData = new InquiryItemData();

        itemData.setUserIdx(inquiryEntity.getUserEntity().getUserIdx());
        itemData.setInquiryIdx(inquiryEntity.getInquiryIdx());
        itemData.setInquiryTitle(inquiryEntity.getInquiryTitle());
        itemData.setInquiryCreatedDate(inquiryEntity.getInquiryCreatedDate());
        itemData.setInquiryStatus(inquiryEntity.getInquiryStatus());
        itemData.setInquiryType(inquiryEntity.getInquiryType());

        return itemData;
    }

    @IterableMapping(qualifiedByName = "I2I")
    List<InquiryItemData> inquirysToItemDatas(List<InquiryEntity> inquiryEntityList);
}
