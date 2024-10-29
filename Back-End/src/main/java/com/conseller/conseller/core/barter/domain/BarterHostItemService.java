package com.conseller.conseller.core.barter.domain;

import com.conseller.conseller.core.barter.infrastructure.BarterHostItemRepository;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterEntity;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterHostItemEntity;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonEntity;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonRepository;
import com.conseller.conseller.core.gifticon.infrastructure.enums.GifticonStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BarterHostItemService implements BarterHostItemService{

    private final BarterHostItemRepository barterHostItemRepository;
    private final GifticonRepository gifticonRepository;
    @Override
    public LocalDateTime addBarterHostItem(List<Long> gifticons, BarterEntity barterEntity) {
        for(Long gifticonIdx : gifticons) {
            GifticonEntity gifticonEntity = gifticonRepository.findById(gifticonIdx)
                    .orElseThrow(()-> new RuntimeException("존재하지 않는 기프티콘입니다."));
            if(!gifticonEntity.getGifticonStatus().equals(GifticonStatus.KEEP.getStatus())) {
                throw new RuntimeException("보관 상태인 기프티콘만 등록할 수 있습니다.");
            }
        }

        LocalDateTime recentExpireDate = null;


        for(Long gifticonIdx : gifticons) {
            GifticonEntity gifticonEntity = gifticonRepository.findById(gifticonIdx)
                    .orElseThrow(()-> new RuntimeException("존재하지 않는 기프티콘입니다."));
            if(recentExpireDate == null || gifticonEntity.getGifticonEndDate().isBefore(recentExpireDate)) {
                recentExpireDate = gifticonEntity.getGifticonEndDate();
            }

            BarterHostItemEntity barterHostItemEntity = new BarterHostItemEntity(barterEntity, gifticonEntity);
            gifticonEntity.setGifticonStatus(GifticonStatus.BARTER.getStatus());
            barterHostItemRepository.save(barterHostItemEntity);
        }

        return recentExpireDate;
    }
}
