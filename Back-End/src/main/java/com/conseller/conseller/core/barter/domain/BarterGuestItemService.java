package com.conseller.conseller.core.barter.domain;

import com.conseller.conseller.core.barter.infrastructure.BarterGuestItemRepository;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterGuestItemEntity;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterRequestEntity;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonEntity;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonRepository;
import com.conseller.conseller.core.gifticon.domain.enums.GifticonStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BarterGuestItemService {

    private final BarterGuestItemRepository barterGuestItemRepository;
    private final GifticonRepository gifticonRepository;

    public Void addBarterGuestItem(List<Long> gifticonList, BarterRequestEntity barterRequestEntity) {
        for(Long gifticonIdx : gifticonList) {
            GifticonEntity gifticonEntity = gifticonRepository.findById(gifticonIdx)
                    .orElseThrow(()-> new RuntimeException("존재하지 않는 기프티콘입니다."));
            if(!gifticonEntity.getGifticonStatus().equals(GifticonStatus.KEEP.getStatus())) {
                throw new RuntimeException("보관 상태인 기프티콘만 등록할 수 있습니다.");
            }
        }


        for(Long gifticonIdx : gifticonList) {
            GifticonEntity gifticonEntity = gifticonRepository.findById(gifticonIdx)
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 기프티콘입니다."));
            BarterGuestItemEntity barterGuestItemEntity = new BarterGuestItemEntity(barterRequestEntity, gifticonEntity);
            gifticonEntity.setGifticonStatus(GifticonStatus.BARTER.getStatus());
            barterGuestItemRepository.save(barterGuestItemEntity);
        }
        return null;
    }
}
