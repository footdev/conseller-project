package com.conseller.conseller.auction.domain;

import com.conseller.conseller.auction.api.dto.request.AuctionListRequest;
import com.conseller.conseller.auction.api.dto.request.ModifyAuctionRequest;
import com.conseller.conseller.auction.api.dto.request.RegistAuctionRequest;
import com.conseller.conseller.auction.api.dto.response.*;
import com.conseller.conseller.auction.infrastructure.AuctionEntity;

import java.util.List;

public interface AuctionService {

    public AuctionListResponse getAuctionList(long auctionId, AuctionListRequest request);

    public Long registAuction(RegistAuctionRequest request);

    public DetailAuctionResponse detailAuction(Long auctionIdx);

    public void modifyAuction(Long auctionIdx, ModifyAuctionRequest auctionRequest);

    public void deleteAuction(Long auctionIdx);

    public AuctionTradeResponse tradeAuction(Long auctionIdx, Long userIdx);

    public void cancelAuction(Long auctionIdx);

    public void confirmAuction(Long auctionIdx);

    public AuctionConfirmResponse getConfirmAuction(Long auctionIdx);

    public AuctionConfirmBuyResponse getConfirmBuyAuction(Long auctionIdx);

    public List<AuctionEntity> getAuctionConfirmList();

    public List<AuctionEntity> getAuctionExpiredList();

    public void rejectAuction(Long auctionIdx);

    public List<AuctionEntity> getPopularAuction();

    public List<Integer> getMainCategory();

    public List<Integer> getSubCategory();
}
