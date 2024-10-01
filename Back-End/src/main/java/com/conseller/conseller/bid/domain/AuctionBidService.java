package com.conseller.conseller.bid.domain;

import com.conseller.conseller.bid.api.dto.request.AuctionBidRequest;

public interface AuctionBidService {
    public void registAuctionBid(Long auctionIdx, AuctionBidRequest request);

    public void deleteAuctionBid(Long auctionBidIdx);

    public void rejectAuctionBid(Long auctionBidIdx);
}
