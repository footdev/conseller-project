package com.conseller.conseller.core.bid.api.dto.mapper;

import com.conseller.conseller.core.bid.api.dto.request.AuctionBidRequest;
import com.conseller.conseller.core.auction.infrastructure.AuctionEntity;
import com.conseller.conseller.core.bid.infrastructure.BiddingEntity;
import com.conseller.conseller.core.user.infrastructure.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AuctionBidMapper {
    AuctionBidMapper INSTANCE = Mappers.getMapper(AuctionBidMapper.class);

    //User, Auction, requset -> AuctionBid Entity 매핑
    @Mapping(source = "user", target = "user")
    @Mapping(source = "auction", target = "auction")
    BiddingEntity registRequestToAuctionBid(UserEntity userEntity, AuctionEntity auctionEntity, AuctionBidRequest request);

    @Mapping(source = "user", target = "user")
    @Mapping(source = "auction", target = "auction")
    BiddingEntity registImToAuctionBid(UserEntity userEntity, AuctionEntity auctionEntity, Integer auctionBidPrice);
}
