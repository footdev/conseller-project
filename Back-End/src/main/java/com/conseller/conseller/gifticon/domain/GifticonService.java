package com.conseller.conseller.gifticon.domain;

import com.conseller.conseller.gifticon.api.dto.response.ExpiringGifticonResponse;
import com.conseller.conseller.gifticon.api.dto.response.GifticonResponse;
import com.conseller.conseller.gifticon.api.dto.request.GifticonRegisterRequest;

import java.util.List;

public interface GifticonService {
    public GifticonResponse getGifticonResponse(long gifticonIdx);

    public void registGifticon(long userIdx, GifticonRegisterRequest gifticonRegisterRequest, String allImageUrl, String dataImageUrl);

    public void deleteGifticon(long gifticonIdx);

    public List<ExpiringGifticonResponse> getGifticonExpirationDates();
}
