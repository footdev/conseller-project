package com.conseller.conseller.store.api.dto.mapper;

import com.conseller.conseller.entity.Gifticon;
import com.conseller.conseller.entity.Store;
import com.conseller.conseller.user.infrastructure.UserEntity;
import com.conseller.conseller.store.api.dto.request.RegistStoreRequest;
import com.conseller.conseller.store.api.dto.response.DetailStoreResponse;
import com.conseller.conseller.store.api.dto.response.StoreConfirmResponse;
import com.conseller.conseller.store.api.dto.response.StoreItemData;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;

import static com.conseller.conseller.utils.DateTimeConverter.convertString;

@Mapper(componentModel="spring")
public interface StoreMapper {
    StoreMapper INSTANCE = Mappers.getMapper(StoreMapper.class);

    //RegistSaleRequest -> Store 매핑
    @Mapping(source = "user", target = "user")
    @Mapping(source = "gifticon", target = "gifticon")
    Store registStoreRequestToStore(RegistStoreRequest request, UserEntity userEntity, Gifticon gifticon);

    //User, Store -> DetailStoreResponse 매핑
    default DetailStoreResponse entityToDetailStoreResponse(Store store) {
        DetailStoreResponse response = new DetailStoreResponse();

        response.setPostContent(store.getStoreText());
        response.setStoreUserIdx(store.getUserEntity().getUserIdx());
        response.setStoreUserNickname(store.getUserEntity().getUserNickname());
        response.setStoreUserProfileUrl(store.getUserEntity().getUserProfileUrl());
        response.setStoreUserDeposit(store.getUserEntity().getUserDeposit());
        response.setStoreIdx(store.getStoreIdx());
        response.setGifticonDataImageName(store.getGifticon().getGifticonDataImageUrl());
        response.setGifticonName(store.getGifticon().getGifticonName());
        response.setGifticonEndDate(convertString(store.getGifticon().getGifticonEndDate()));
        response.setStoreEndDate(convertString(store.getStoreEndDate()));
        response.setDeposit(false);
        response.setStorePrice(store.getStorePrice());

        return response;
    }

    //StoreList -> StoreItemDataList 매핑
    @Named("S2S")
    default StoreItemData storeToItemData(Store store) {
        StoreItemData itemData = new StoreItemData();

        itemData.setStoreIdx(store.getStoreIdx());
        itemData.setGifticonDataImageName(store.getGifticon().getGifticonDataImageUrl());
        itemData.setGifticonName(store.getGifticon().getGifticonName());
        itemData.setGifticonEndDate(convertString(store.getGifticon().getGifticonEndDate()));
        itemData.setStoreEndDate(convertString(store.getStoreEndDate()));
        itemData.setStoreStatus(store.getStoreStatus());
        itemData.setDeposit(false);
        itemData.setStorePrice(store.getStorePrice());

        return itemData;
    }

    @IterableMapping(qualifiedByName = "S2S")
    List<StoreItemData> storesToItemDatas(List<Store> storeList);

    //Store -> StoreConfirm 매핑
    default StoreConfirmResponse storeToComfirm(Store store) {
        StoreConfirmResponse response = new StoreConfirmResponse();

        response.setGifticonDataImageName(store.getGifticon().getGifticonDataImageUrl());
        response.setNotificationCreatedDate(convertString(LocalDateTime.now()));
        response.setGiftconName(store.getGifticon().getGifticonName());
        response.setStorePrice(store.getStorePrice());
        response.setPostContent(store.getStoreText());
        response.setBuyUserImageUrl(store.getConsumer().getUserProfileUrl());
        response.setBuyUserNickname(store.getConsumer().getUserNickname());
        response.setBuyUserName(store.getConsumer().getUsername());
        response.setBuyUserIdx(store.getUserEntity().getUserIdx());

        return response;
    }
}
