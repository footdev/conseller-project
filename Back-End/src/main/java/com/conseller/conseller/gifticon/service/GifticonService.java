package com.conseller.conseller.gifticon.service;

import com.conseller.conseller.gifticon.dto.response.ExpiringGifticonResponse;
import com.conseller.conseller.gifticon.dto.response.GifticonResponse;
import com.conseller.conseller.gifticon.dto.request.GifticonRegisterRequest;
import com.conseller.conseller.gifticon.dto.response.ImageUrlsResponse;

import java.util.List;

public interface GifticonService {
    public GifticonResponse getGifticonResponse(long gifticonIdx);

    public void registGifticon(long userIdx, GifticonRegisterRequest gifticonRegisterRequest, String allImageUrl, String dataImageUrl);

    public void deleteGifticon(long gifticonIdx);

    public List<ExpiringGifticonResponse> getGifticonExpirationDates();
}
