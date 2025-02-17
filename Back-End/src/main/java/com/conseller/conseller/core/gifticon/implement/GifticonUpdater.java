package com.conseller.conseller.core.gifticon.implement;

import com.conseller.conseller.core.gifticon.domain.Gifticon;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonEntity;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonRepository;
import com.conseller.conseller.core.gifticon.domain.enums.GifticonStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GifticonUpdater {
    private final GifticonRepository gifticonRepository;

    public void updateToKeepStatus(Gifticon gifticon) {
        gifticon.updateStatus(GifticonStatus.KEEP);
        gifticonRepository.save(GifticonEntity.of(gifticon));
    }
}
