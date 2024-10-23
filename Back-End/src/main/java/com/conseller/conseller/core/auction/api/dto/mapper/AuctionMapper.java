package com.conseller.conseller.core.auction.api.dto.mapper;

import com.conseller.conseller.core.auction.api.dto.request.RegistAuctionRequest;
import com.conseller.conseller.core.auction.api.dto.response.*;
import com.conseller.conseller.core.auction.infrastructure.AuctionEntity;
import com.conseller.conseller.core.bid.infrastructure.AuctionBidEntity;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonEntity;
import com.conseller.conseller.core.user.infrastructure.UserEntity;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;

import static com.conseller.conseller.global.utils.DateTimeConverter.convertString;

@Mapper(componentModel="spring")
public interface AuctionMapper {
    AuctionMapper INSTANCE = Mappers.getMapper(AuctionMapper.class);

    // RegistAuctionRequest -> Auction 매핑
    @Mapping(source = "user", target = "user")
    @Mapping(source = "gifticon", target = "gifticon")
    AuctionEntity registAuctionRequestToAuctionEntity(RegistAuctionRequest registAuctionRequest, UserEntity userEntity, GifticonEntity gifticonEntity);

    //User, Auction, AuctionBidList -> DetailAuctionResponse 매핑
    default DetailAuctionResponse entityToDetailAuctionResponse(AuctionEntity auctionEntity, List<AuctionBidItemData> auctionBidList) {
        DetailAuctionResponse response = new DetailAuctionResponse();

        response.setAuctionBidList(auctionBidList);
        response.setAuctionIdx(auctionEntity.getAuctionIdx());
        response.setAuctionUserIdx(auctionEntity.getUserEntity().getUserIdx());
        response.setAuctionUserDeposit(auctionEntity.getUserEntity().getUserDeposit());
        response.setAuctionUserNickname(auctionEntity.getUserEntity().getUserNickname());
        response.setDeposit(false);
        response.setAuctionEndDate(convertString(auctionEntity.getAuctionEndDate()));
        response.setAuctionHighestBid(auctionEntity.getHighestBidEntity().getAuctionBidPrice());
        response.setAuctionUserProfileUrl(auctionEntity.getUserEntity().getUserProfileUrl());
        response.setGifticonDataImageName(auctionEntity.getGifticonEntity().getGifticonDataImageUrl());
        response.setGifticonName(auctionEntity.getGifticonEntity().getGifticonName());
        response.setGifticonEndDate(convertString(auctionEntity.getGifticonEntity().getGifticonEndDate()));
        response.setPostContent(auctionEntity.getAuctionText());
        response.setLowerPrice(auctionEntity.getLowerPrice());
        response.setUpperPrice(auctionEntity.getUpperPrice());

        return response;
    }

    //AuctionList -> AuctionItemDataList 매핑
    @Named("A2A")
    default AuctionItemData auctionToItemData(AuctionEntity auctionEntity){
        AuctionItemData itemData = new AuctionItemData();
        itemData.setAuctionIdx(auctionEntity.getAuctionIdx());
        itemData.setGifticonDataImageName(auctionEntity.getGifticonEntity().getGifticonDataImageUrl());
        itemData.setGifticonName(auctionEntity.getGifticonEntity().getGifticonName());
        itemData.setGifticonEndDate(convertString(auctionEntity.getGifticonEntity().getGifticonEndDate()));
        itemData.setAuctionEndDate(convertString(auctionEntity.getAuctionEndDate()));
        itemData.setAuctionStatus(auctionEntity.getAuctionStatus());
        itemData.setDeposit(false);
        itemData.setUpperPrice(auctionEntity.getUpperPrice());
        itemData.setLowerPrice(auctionEntity.getLowerPrice());

        return itemData;
    }

    @IterableMapping(qualifiedByName = "A2A")
    List<AuctionItemData> auctionsToItemDatas(List<AuctionEntity> auctionEntityList);

    //AuctionBidList -> AuctionBidItemDataList 매핑
    @Named("B2B")
    default AuctionBidItemData bidToItemData(AuctionBidEntity auctionBidEntity) {
        AuctionBidItemData itemData = new AuctionBidItemData();

        itemData.setAuctionBidIdx(auctionBidEntity.getAuctionBidIdx());
        itemData.setAuctionBidPrice(auctionBidEntity.getAuctionBidPrice());
        itemData.setAuctionRegistedDate(convertString(auctionBidEntity.getAuctionRegistedDate()));
        itemData.setAuctionBidStatus(auctionBidEntity.getAuctionBidStatus());
        itemData.setUserIdx(auctionBidEntity.getUserEntity().getUserIdx());
        itemData.setAuctionIdx(auctionBidEntity.getAuctionEntity().getAuctionIdx());

        return itemData;
    }

    @IterableMapping(qualifiedByName = "B2B")
    List<AuctionBidItemData> bidsToItemDatas(List<AuctionBidEntity> auctionBidEntityList);

    // auction -> auctionConfirmResponse 매핑
    default AuctionConfirmResponse auctionToConfirm(AuctionEntity auctionEntity) {
        AuctionConfirmResponse response = new AuctionConfirmResponse();

        response.setGifticonDataImageName(auctionEntity.getGifticonEntity().getGifticonDataImageUrl());
        response.setNotificationCreatedDate(convertString(LocalDateTime.now()));
        response.setGiftconName(auctionEntity.getGifticonEntity().getGifticonName());
        response.setPostContent(auctionEntity.getAuctionText());

        return response;
    }

    // auction -> auctionConfirmBuyResponse 매핑
    default AuctionConfirmBuyResponse auctionToConfirmBuy(AuctionEntity auctionEntity) {
        AuctionConfirmBuyResponse response = new AuctionConfirmBuyResponse();

        response.setGifticonDataImageName(auctionEntity.getGifticonEntity().getGifticonDataImageUrl());
        response.setGiftconName(auctionEntity.getGifticonEntity().getGifticonName());
        response.setPostContent(auctionEntity.getAuctionText());
        response.setUserName(auctionEntity.getUserEntity().getUsername());
        response.setUserAccount(auctionEntity.getUserEntity().getUserAccount());
        response.setUserAccountBank(auctionEntity.getUserEntity().getUserAccountBank());

        return response;
    }
}
