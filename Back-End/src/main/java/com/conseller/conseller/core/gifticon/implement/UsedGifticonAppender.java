package com.conseller.conseller.core.gifticon.implement;

import com.conseller.conseller.core.gifticon.domain.UsedGifticon;
import com.conseller.conseller.core.gifticon.infrastructure.UsedGifticonEntity;
import com.conseller.conseller.core.gifticon.infrastructure.UsedGifticonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsedGifticonAppender {
    private final UsedGifticonRepository usedGifticonRepository;

    public void append(UsedGifticon usedGifticon) {
        usedGifticonRepository.save(UsedGifticonEntity.of(usedGifticon.getUsedGifticonBarcode()));
    }
}
