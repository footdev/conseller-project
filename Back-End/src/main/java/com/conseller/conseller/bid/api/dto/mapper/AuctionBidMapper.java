package com.conseller.conseller.bid.api.dto.mapper;

import com.conseller.conseller.bid.api.dto.request.AuctionBidRequest;
import com.conseller.conseller.auction.infrastructure.AuctionEntity;
import com.conseller.conseller.entity.AuctionBid;
import com.conseller.conseller.user.infrastructure.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AuctionBidMapper {
    AuctionBidMapper INSTANCE = Mappers.getMapper(AuctionBidMapper.class);

    //User, Auction, requset -> AuctionBid Entity 매핑
    @Mapping(source = "user", target = "user")
    @Mapping(source = "auction", target = "auction")
    AuctionBid registRequestToAuctionBid(UserEntity userEntity, AuctionEntity auctionEntity, AuctionBidRequest request);

    @Mapping(source = "user", target = "user")
    @Mapping(source = "auction", target = "auction")
    AuctionBid registImToAuctionBid(UserEntity userEntity, AuctionEntity auctionEntity, Integer auctionBidPrice);
}
