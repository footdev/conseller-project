package com.conseller.conseller.core.gifticon.implement;

import com.conseller.conseller.core.gifticon.domain.Gifticon;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonEntity;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonRepository;
import com.conseller.conseller.core.gifticon.domain.enums.GifticonStatus;
import com.conseller.conseller.core.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GifticonModifier {
    private final GifticonRepository gifticonRepository;

    public void changeStatusToKeep(Gifticon gifticon) {
        gifticon.updateStatus(GifticonStatus.KEEP);
        gifticonRepository.save(GifticonEntity.of(gifticon));
    }

    public void changeAllStatusToKeep(List<Gifticon> gifticons) {
        gifticons.forEach(this::changeStatusToKeep);
        gifticonRepository.saveAll(gifticons.stream()
                .map(GifticonEntity::of)
                .collect(Collectors.toList()));
    }

    public void transfer(Gifticon gifticon, User buyer) {
        gifticon.transfer(buyer);
        gifticonRepository.save(GifticonEntity.of(gifticon));
    }
}
