package com.conseller.conseller.inquiry.api.dto.mapper;

import com.conseller.conseller.entity.Inquiry;
import com.conseller.conseller.user.infrastructure.UserEntity;
import com.conseller.conseller.inquiry.api.dto.request.RegistInquiryRequest;
import com.conseller.conseller.inquiry.api.dto.response.DetailInquiryResponse;
import com.conseller.conseller.inquiry.api.dto.response.InquiryItemData;
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
    Inquiry registInquiryToInquiry(RegistInquiryRequest request, UserEntity userEntity, UserEntity reportedUserEntity);

    //User, Inquiry -> DetailInquiryResponse
    @Mapping(source = "user.userIdx", target = "userIdx")
    DetailInquiryResponse entityToDetailInquiryResponse(UserEntity userEntity, Inquiry inquiry);

    //InquiryList -> InquiryItemDataList 매핑
    @Named("I2I")
    default InquiryItemData inquiryToItemData(Inquiry inquiry){
        InquiryItemData itemData = new InquiryItemData();

        itemData.setUserIdx(inquiry.getUserEntity().getUserIdx());
        itemData.setInquiryIdx(inquiry.getInquiryIdx());
        itemData.setInquiryTitle(inquiry.getInquiryTitle());
        itemData.setInquiryCreatedDate(inquiry.getInquiryCreatedDate());
        itemData.setInquiryStatus(inquiry.getInquiryStatus());
        itemData.setInquiryType(inquiry.getInquiryType());

        return itemData;
    }

    @IterableMapping(qualifiedByName = "I2I")
    List<InquiryItemData> inquirysToItemDatas(List<Inquiry> inquiryList);
}
