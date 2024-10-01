package com.conseller.conseller.barter.domain;

import com.conseller.conseller.barter.infrastructure.BarterHostItemRepository;
import com.conseller.conseller.barter.infrastructure.Barter;
import com.conseller.conseller.barter.infrastructure.BarterHostItem;
import com.conseller.conseller.entity.Gifticon;
import com.conseller.conseller.gifticon.infrastructure.GifticonRepository;
import com.conseller.conseller.gifticon.domain.enums.GifticonStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BarterHostItemServiceImpl implements BarterHostItemService{

    private final BarterHostItemRepository barterHostItemRepository;
    private final GifticonRepository gifticonRepository;
    @Override
    public LocalDateTime addBarterHostItem(List<Long> gifticons, Barter barter) {
        for(Long gifticonIdx : gifticons) {
            Gifticon gifticon = gifticonRepository.findById(gifticonIdx)
                    .orElseThrow(()-> new RuntimeException("존재하지 않는 기프티콘입니다."));
            if(!gifticon.getGifticonStatus().equals(GifticonStatus.KEEP.getStatus())) {
                throw new RuntimeException("보관 상태인 기프티콘만 등록할 수 있습니다.");
            }
        }

        LocalDateTime recentExpireDate = null;


        for(Long gifticonIdx : gifticons) {
            Gifticon gifticon = gifticonRepository.findById(gifticonIdx)
                    .orElseThrow(()-> new RuntimeException("존재하지 않는 기프티콘입니다."));
            if(recentExpireDate == null || gifticon.getGifticonEndDate().isBefore(recentExpireDate)) {
                recentExpireDate = gifticon.getGifticonEndDate();
            }

            BarterHostItem barterHostItem = new BarterHostItem(barter, gifticon);
            gifticon.setGifticonStatus(GifticonStatus.BARTER.getStatus());
            barterHostItemRepository.save(barterHostItem);
        }

        return recentExpireDate;
    }
}
