package com.conseller.conseller.core.store.api.dto.mapper;

import com.conseller.conseller.core.gifticon.infrastructure.GifticonEntity;
import com.conseller.conseller.core.store.infrastructure.StoreEntity;
import com.conseller.conseller.core.user.infrastructure.UserEntity;
import com.conseller.conseller.core.store.api.dto.request.RegistStoreRequest;
import com.conseller.conseller.core.store.api.dto.response.DetailStoreResponse;
import com.conseller.conseller.core.store.api.dto.response.StoreConfirmResponse;
import com.conseller.conseller.core.store.api.dto.response.StoreItemData;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;

import static com.conseller.conseller.global.utils.DateTimeConverter.convertString;

@Mapper(componentModel="spring")
public interface StoreMapper {
    StoreMapper INSTANCE = Mappers.getMapper(StoreMapper.class);

    //RegistSaleRequest -> Store 매핑
    @Mapping(source = "user", target = "user")
    @Mapping(source = "gifticon", target = "gifticon")
    StoreEntity registStoreRequestToStore(RegistStoreRequest request, UserEntity userEntity, GifticonEntity gifticonEntity);

    //User, Store -> DetailStoreResponse 매핑
    default DetailStoreResponse entityToDetailStoreResponse(StoreEntity storeEntity) {
        DetailStoreResponse response = new DetailStoreResponse();

        response.setPostContent(storeEntity.getStoreText());
        response.setStoreUserIdx(storeEntity.getUserEntity().getUserIdx());
        response.setStoreUserNickname(storeEntity.getUserEntity().getUserNickname());
        response.setStoreUserProfileUrl(storeEntity.getUserEntity().getUserProfileUrl());
        response.setStoreUserDeposit(storeEntity.getUserEntity().getUserDeposit());
        response.setStoreIdx(storeEntity.getStoreIdx());
        response.setGifticonDataImageName(storeEntity.getGifticonEntity().getGifticonDataImageUrl());
        response.setGifticonName(storeEntity.getGifticonEntity().getGifticonName());
        response.setGifticonEndDate(convertString(storeEntity.getGifticonEntity().getGifticonEndDate()));
        response.setStoreEndDate(convertString(storeEntity.getStoreEndDate()));
        response.setDeposit(false);
        response.setStorePrice(storeEntity.getStorePrice());

        return response;
    }

    //StoreList -> StoreItemDataList 매핑
    @Named("S2S")
    default StoreItemData storeToItemData(StoreEntity storeEntity) {
        StoreItemData itemData = new StoreItemData();

        itemData.setStoreIdx(storeEntity.getStoreIdx());
        itemData.setGifticonDataImageName(storeEntity.getGifticonEntity().getGifticonDataImageUrl());
        itemData.setGifticonName(storeEntity.getGifticonEntity().getGifticonName());
        itemData.setGifticonEndDate(convertString(storeEntity.getGifticonEntity().getGifticonEndDate()));
        itemData.setStoreEndDate(convertString(storeEntity.getStoreEndDate()));
        itemData.setStoreStatus(storeEntity.getStoreStatus());
        itemData.setDeposit(false);
        itemData.setStorePrice(storeEntity.getStorePrice());

        return itemData;
    }

    @IterableMapping(qualifiedByName = "S2S")
    List<StoreItemData> storesToItemDatas(List<StoreEntity> storeEntityList);

    //Store -> StoreConfirm 매핑
    default StoreConfirmResponse storeToComfirm(StoreEntity storeEntity) {
        StoreConfirmResponse response = new StoreConfirmResponse();

        response.setGifticonDataImageName(storeEntity.getGifticonEntity().getGifticonDataImageUrl());
        response.setNotificationCreatedDate(convertString(LocalDateTime.now()));
        response.setGiftconName(storeEntity.getGifticonEntity().getGifticonName());
        response.setStorePrice(storeEntity.getStorePrice());
        response.setPostContent(storeEntity.getStoreText());
        response.setBuyUserImageUrl(storeEntity.getConsumerEntity().getUserProfileUrl());
        response.setBuyUserNickname(storeEntity.getConsumerEntity().getUserNickname());
        response.setBuyUserName(storeEntity.getConsumerEntity().getUsername());
        response.setBuyUserIdx(storeEntity.getUserEntity().getUserIdx());

        return response;
    }
}
