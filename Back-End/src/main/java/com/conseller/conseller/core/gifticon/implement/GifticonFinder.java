package com.conseller.conseller.core.gifticon.implement;

import com.conseller.conseller.core.gifticon.api.dto.response.ExpiringGifticonResponse;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GifticonFinder {
    private final GifticonRepositoryImpl gifticonRepositoryImpl;

    public List<ExpiringGifticonResponse> getGifticonsExpiringCnt() {
        return gifticonRepositoryImpl.getUserIdxAndExpiringGifticonCount();
    }
}
