package com.conseller.conseller.core.gifticon.implement;

import com.conseller.conseller.core.gifticon.domain.Gifticon;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonEntity;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GifticonAppender {
    private final GifticonRepository gifticonRepository;

    public void append(Gifticon gifticon) {
        gifticonRepository.save(GifticonEntity.of(gifticon));
    }
}
