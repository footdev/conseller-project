package com.conseller.conseller.auction.api.dto.mapper;

import com.conseller.conseller.auction.api.dto.request.RegistAuctionRequest;
import com.conseller.conseller.auction.api.dto.response.*;
import com.conseller.conseller.auction.auction.dto.response.*;
import com.conseller.conseller.auction.infrastructure.Auction;
import com.conseller.conseller.entity.AuctionBid;
import com.conseller.conseller.entity.Gifticon;
import com.conseller.conseller.entity.User;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;

import static com.conseller.conseller.utils.DateTimeConverter.*;

@Mapper(componentModel="spring")
public interface AuctionMapper {
    AuctionMapper INSTANCE = Mappers.getMapper(AuctionMapper.class);

    // RegistAuctionRequest -> Auction 매핑
    @Mapping(source = "user", target = "user")
    @Mapping(source = "gifticon", target = "gifticon")
    Auction registAuctionRequestToAuction(RegistAuctionRequest registAuctionRequest, User user, Gifticon gifticon);

    //User, Auction, AuctionBidList -> DetailAuctionResponse 매핑
    default DetailAuctionResponse entityToDetailAuctionResponse(Auction auction, List<AuctionBidItemData> auctionBidList) {
        DetailAuctionResponse response = new DetailAuctionResponse();

        response.setAuctionBidList(auctionBidList);
        response.setAuctionIdx(auction.getAuctionIdx());
        response.setAuctionUserIdx(auction.getUser().getUserIdx());
        response.setAuctionUserDeposit(auction.getUser().getUserDeposit());
        response.setAuctionUserNickname(auction.getUser().getUserNickname());
        response.setDeposit(false);
        response.setAuctionEndDate(convertString(auction.getAuctionEndDate()));
        response.setAuctionHighestBid(auction.getHighestBid().getAuctionBidPrice());
        response.setAuctionUserProfileUrl(auction.getUser().getUserProfileUrl());
        response.setGifticonDataImageName(auction.getGifticon().getGifticonDataImageUrl());
        response.setGifticonName(auction.getGifticon().getGifticonName());
        response.setGifticonEndDate(convertString(auction.getGifticon().getGifticonEndDate()));
        response.setPostContent(auction.getAuctionText());
        response.setLowerPrice(auction.getLowerPrice());
        response.setUpperPrice(auction.getUpperPrice());

        return response;
    }

    //AuctionList -> AuctionItemDataList 매핑
    @Named("A2A")
    default AuctionItemData auctionToItemData(Auction auction){
        AuctionItemData itemData = new AuctionItemData();
        itemData.setAuctionIdx(auction.getAuctionIdx());
        itemData.setGifticonDataImageName(auction.getGifticon().getGifticonDataImageUrl());
        itemData.setGifticonName(auction.getGifticon().getGifticonName());
        itemData.setGifticonEndDate(convertString(auction.getGifticon().getGifticonEndDate()));
        itemData.setAuctionEndDate(convertString(auction.getAuctionEndDate()));
        itemData.setAuctionStatus(auction.getAuctionStatus());
        itemData.setDeposit(false);
        itemData.setUpperPrice(auction.getUpperPrice());
        itemData.setLowerPrice(auction.getLowerPrice());

        return itemData;
    }

    @IterableMapping(qualifiedByName = "A2A")
    List<AuctionItemData> auctionsToItemDatas(List<Auction> auctionList);

    //AuctionBidList -> AuctionBidItemDataList 매핑
    @Named("B2B")
    default AuctionBidItemData bidToItemData(AuctionBid auctionBid) {
        AuctionBidItemData itemData = new AuctionBidItemData();

        itemData.setAuctionBidIdx(auctionBid.getAuctionBidIdx());
        itemData.setAuctionBidPrice(auctionBid.getAuctionBidPrice());
        itemData.setAuctionRegistedDate(convertString(auctionBid.getAuctionRegistedDate()));
        itemData.setAuctionBidStatus(auctionBid.getAuctionBidStatus());
        itemData.setUserIdx(auctionBid.getUser().getUserIdx());
        itemData.setAuctionIdx(auctionBid.getAuction().getAuctionIdx());

        return itemData;
    }

    @IterableMapping(qualifiedByName = "B2B")
    List<AuctionBidItemData> bidsToItemDatas(List<AuctionBid> auctionBidList);

    // auction -> auctionConfirmResponse 매핑
    default AuctionConfirmResponse auctionToConfirm(Auction auction) {
        AuctionConfirmResponse response = new AuctionConfirmResponse();

        response.setGifticonDataImageName(auction.getGifticon().getGifticonDataImageUrl());
        response.setNotificationCreatedDate(convertString(LocalDateTime.now()));
        response.setGiftconName(auction.getGifticon().getGifticonName());
        response.setPostContent(auction.getAuctionText());

        return response;
    }

    // auction -> auctionConfirmBuyResponse 매핑
    default AuctionConfirmBuyResponse auctionToConfirmBuy(Auction auction) {
        AuctionConfirmBuyResponse response = new AuctionConfirmBuyResponse();

        response.setGifticonDataImageName(auction.getGifticon().getGifticonDataImageUrl());
        response.setGiftconName(auction.getGifticon().getGifticonName());
        response.setPostContent(auction.getAuctionText());
        response.setUserName(auction.getUser().getUsername());
        response.setUserAccount(auction.getUser().getUserAccount());
        response.setUserAccountBank(auction.getUser().getUserAccountBank());

        return response;
    }
}
