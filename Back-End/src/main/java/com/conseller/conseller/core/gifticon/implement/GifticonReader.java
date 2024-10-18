package com.conseller.conseller.core.gifticon.implement;

import com.conseller.conseller.core.gifticon.domain.Gifticon;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonEntity;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonRepository;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GifticonReader {

    private GifticonRepository gifticonRepository;

    public Gifticon readGifticon(long gifticonIdx) {
        return gifticonRepository.findByGifticonIdx(gifticonIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.GIFTICON_INVALID))
                .toDomain();
    }
}
