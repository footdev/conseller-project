package com.conseller.conseller.core.gifticon.implement;

import com.conseller.conseller.core.gifticon.api.dto.response.ExpiringGifticonResponse;
import com.conseller.conseller.core.gifticon.domain.Gifticon;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonRepository;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonRepositoryImpl;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GifticonFinder {
    private final GifticonRepository gifticonRepository;
    private final GifticonRepositoryImpl gifticonRepositoryImpl;
    private final GifticonValidator gifticonValidator;

    public Gifticon findKeepGifticon(long gifticonIdx) {
        Gifticon gifticon = gifticonRepository.findByGifticonIdx(gifticonIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.INVALID_GIFTICON))
                .toDomain();

        gifticonValidator.isKeep(gifticon);
        gifticonValidator.isValidRegistGiftion(gifticon);
        return gifticon;
    }

    public List<ExpiringGifticonResponse> getGifticonsExpiringCnt() {
        return gifticonRepositoryImpl.getUserIdxAndExpiringGifticonCount();
    }
}
